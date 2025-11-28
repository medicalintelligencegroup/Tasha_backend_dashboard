package com.mig.patientservice.service.patient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mig.patientservice.exception.PatientNotFoundException;
import com.mig.patientservice.persistence.patient.Annotation;
import com.mig.patientservice.persistence.patient.AnnotationRepository;
import com.mig.patientservice.persistence.patient.ClinicianNotes;
import com.mig.patientservice.persistence.patient.Patient;
import com.mig.patientservice.persistence.patient.PatientRepository;
import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.persistence.patient.ScanRepository;
import com.mig.patientservice.web.patient.request.CreatePatient;
import com.mig.patientservice.web.patient.response.AnnotationResponse;
import com.mig.patientservice.web.patient.response.PatientResponse;
import com.mig.patientservice.web.patient.response.ScanResponse;

@Service
public class PatientServiceImpl implements PatientService {
	
	private final PatientRepository patientRepository;
	private final ScanRepository scanRepository;
	private final AnnotationRepository annotationRepository;
	private final PatientResponseConverter patientResponseConverter;
	private final ScanResponseConverter scanResponseConverter;
	private final AnnotationResponseConverter annotationResponseConverter;
	
	public PatientServiceImpl(PatientRepository patientRepository,
			ScanRepository scanRepository,
			AnnotationRepository annotationRepository,
			PatientResponseConverter patientResponseConverter,
			ScanResponseConverter scanResponseConverter,
			AnnotationResponseConverter annotationResponseConverter) {
		this.patientRepository = patientRepository;
		this.scanRepository = scanRepository;
		this.annotationRepository = annotationRepository;
		this.patientResponseConverter = patientResponseConverter;
		this.scanResponseConverter = scanResponseConverter;
		this.annotationResponseConverter = annotationResponseConverter;
	}
	
	public Page<Patient> getPatientList(Pageable pageable) {
		Page<Patient> patientList= patientRepository.findAll(pageable);
		return patientList;
		
	}
	
	public PatientResponse getPatient(String patientId) {
		List<Scan> emptyScanList = new ArrayList<>();
		Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(patientId));
		PatientResponse patientResponse = patientResponseConverter.convert(patient);
		List<Scan> scanList = scanRepository.findByPatientId(patientId).orElse(emptyScanList);
		
		List<ScanResponse> scanResponseList = scanList.stream().map(scanResponseConverter::convert).collect(Collectors.toList());
		
		for(ScanResponse scanResponse: scanResponseList) {
			List<Annotation> emptyAnnotationList = new ArrayList<>();
			List<Annotation> annotationList = annotationRepository.findByScanId(scanResponse.getId()).orElse(emptyAnnotationList);
			List<AnnotationResponse> annotationResponseList = annotationList.stream().map(annotationResponseConverter::convert).collect(Collectors.toList());
			scanResponse.setAnnotationResponseList(annotationResponseList);
		}
		patientResponse.setScanResponseList(scanResponseList);
		return patientResponse;
	}
	
	public Patient createPatient(Patient patient) {
		Patient createdPatient = patientRepository.insert(patient);
		return createdPatient;
	}
	
	public Patient updatePatient(CreatePatient updatePatient, Patient patient) {
		Patient existingPatient = patientRepository.findById(patient.getId()).orElseThrow(() -> new PatientNotFoundException(patient.getId()));
		List<ClinicianNotes> existingClinicianNotes = Optional.ofNullable(existingPatient.getClinicianNotes()).orElse(new ArrayList());
		Optional<String> newNote = Optional.ofNullable(updatePatient.getClinicianNotes());
		if(newNote.isPresent()) {
			ClinicianNotes addNewNote = ClinicianNotes.builder()
										.created(Instant.now())
										.notes(newNote.get())
										.build();
			existingClinicianNotes.add(addNewNote);
			patient.setClinicianNotes(existingClinicianNotes);
		} else {
			patient.setClinicianNotes(existingClinicianNotes);
		}
		patient.setSequenceNumber(existingPatient.getSequenceNumber());
		Patient editedPatient = patientRepository.save(patient);
		return editedPatient;
	}

}

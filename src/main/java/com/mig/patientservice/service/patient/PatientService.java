package com.mig.patientservice.service.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mig.patientservice.persistence.patient.Patient;
import com.mig.patientservice.web.patient.request.CreatePatient;
import com.mig.patientservice.web.patient.response.PatientResponse;

public interface PatientService {
	
	public Page<Patient> getPatientList(Pageable pageable);
	
	public PatientResponse getPatient(String patientId);
	
	public Patient createPatient(Patient patient);
	
	public Patient updatePatient(CreatePatient updatePatient, Patient patient);

}

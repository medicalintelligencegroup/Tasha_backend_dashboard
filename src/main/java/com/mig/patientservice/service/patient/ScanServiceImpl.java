package com.mig.patientservice.service.patient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mig.patientservice.exception.ScanNotFoundException;
import com.mig.patientservice.persistence.patient.Annotation;
import com.mig.patientservice.persistence.patient.AnnotationRepository;
import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.persistence.patient.ScanRepository;
import com.mig.patientservice.web.patient.response.AnnotationResponse;
import com.mig.patientservice.web.patient.response.ScanResponse;

@Service
public class ScanServiceImpl implements ScanService {
	
	private final ScanRepository scanRepository;
	private final AnnotationRepository annotationRepository;
	private final ScanResponseConverter scanResponseConverter;
	private final AnnotationResponseConverter annotationResponseConverter;
	
	public ScanServiceImpl(ScanRepository scanRepository,
			AnnotationRepository annotationRepository,
			ScanResponseConverter scanResponseConverter,
			AnnotationResponseConverter annotationResponseConverter
			) {
		this.scanRepository = scanRepository;
		this.annotationRepository = annotationRepository;
		this.scanResponseConverter = scanResponseConverter;
		this.annotationResponseConverter = annotationResponseConverter;
	}
	
	public Page<Scan> getScanList(Pageable pageable) {
		Page<Scan> scanList= scanRepository.findAll(pageable);
		return scanList;
		
	}
	
	public ScanResponse getScan(String scanId) {
		Scan scan = scanRepository.findById(scanId).orElseThrow(() -> new ScanNotFoundException(scanId));
		List<Annotation> emptyAnnotationList = new ArrayList<>();
		List<Annotation> annotationList = annotationRepository.findByScanId(scanId).orElse(emptyAnnotationList);
		ScanResponse scanResponse = scanResponseConverter.convert(scan);
		List<AnnotationResponse> annotationResponseList = annotationList.stream().map(annotationResponseConverter::convert).collect(Collectors.toList());
		scanResponse.setAnnotationResponseList(annotationResponseList);
		return scanResponse;
	}
	
	public Scan createScan(Scan scan) {
		Scan savedScan = scanRepository.insert(scan);
		return savedScan;
	}

}

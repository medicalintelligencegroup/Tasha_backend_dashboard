package com.mig.patientservice.service.patient;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mig.patientservice.persistence.patient.Annotation;


public interface AnnotationService {
	
	public Page<Annotation> getAnnotationList(Pageable pageable);
	
	public Annotation getAnnotation(String annotationId);
	
	public Annotation createAnnotation(Annotation annotation);

}

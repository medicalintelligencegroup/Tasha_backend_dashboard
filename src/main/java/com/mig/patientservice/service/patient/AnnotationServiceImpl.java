package com.mig.patientservice.service.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mig.patientservice.exception.AnnotationNotFoundException;
import com.mig.patientservice.persistence.patient.Annotation;
import com.mig.patientservice.persistence.patient.AnnotationRepository;

@Service
public class AnnotationServiceImpl implements AnnotationService {
	
	private final AnnotationRepository annotationRepository;
	
	public AnnotationServiceImpl(AnnotationRepository annotationRepository) {
		this.annotationRepository = annotationRepository;
	}
	
	public Page<Annotation> getAnnotationList(Pageable pageable) {
		Page<Annotation> scanList= annotationRepository.findAll(pageable);
		return scanList;
		
	}
	
	public Annotation getAnnotation(String annotationId) {
		return annotationRepository.findById(annotationId).orElseThrow(() -> new AnnotationNotFoundException(annotationId));
	}
	
	public Annotation createAnnotation(Annotation annotation) {
		Annotation savedAnnotation = annotationRepository.insert(annotation);
		return savedAnnotation;
	}

}

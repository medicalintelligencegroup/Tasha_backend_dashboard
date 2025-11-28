package com.mig.patientservice.service.patient;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mig.patientservice.persistence.patient.Annotation;
import com.mig.patientservice.web.patient.request.CreateAnnotation;

@Component
public class AnnotationConverter implements Converter<CreateAnnotation, Annotation> {
	
	private final ModelMapper modelMapper;
	
	public AnnotationConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public Annotation convert(CreateAnnotation createAnnotation) {
		return modelMapper.map(createAnnotation, Annotation.class);
	}

}

package com.mig.patientservice.service.patient;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mig.patientservice.persistence.patient.Annotation;
import com.mig.patientservice.web.patient.response.AnnotationResponse;

@Component
public class AnnotationResponseConverter implements Converter<Annotation, AnnotationResponse> {
	
	private final ModelMapper modelMapper;
	
	public AnnotationResponseConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public AnnotationResponse convert(Annotation annotation) {
		return modelMapper.map(annotation, AnnotationResponse.class);
	}

}

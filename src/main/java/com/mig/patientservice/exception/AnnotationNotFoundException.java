package com.mig.patientservice.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AnnotationNotFoundException extends RuntimeException {
	public AnnotationNotFoundException(String annotationId) {
		super(format("Annotation with id: %s was not found", annotationId));
	}
}

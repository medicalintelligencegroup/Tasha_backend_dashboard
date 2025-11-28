package com.mig.patientservice.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PatientNotFoundException extends RuntimeException {
	public PatientNotFoundException(String patientId) {
		super(format("Patient with id: %s was not found", patientId));
	}
}

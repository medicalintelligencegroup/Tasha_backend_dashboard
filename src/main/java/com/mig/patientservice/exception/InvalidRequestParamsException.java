package com.mig.patientservice.exception;

public class InvalidRequestParamsException extends RuntimeException {

    public InvalidRequestParamsException(final String message) {
        super(message);
    }
}

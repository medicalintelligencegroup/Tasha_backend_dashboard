package com.mig.patientservice.exception;

import static java.lang.String.format;

public class ScanNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1168769881374229765L;

	public ScanNotFoundException(String scanId) {
		super(format("Scan with id: %s was not found", scanId));
	}
}

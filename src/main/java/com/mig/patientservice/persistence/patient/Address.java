package com.mig.patientservice.persistence.patient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
	private String line1;
	private String line2;
	private String line3;
	private String city;
	private String council;
	private String postcode;
	private String country;
}

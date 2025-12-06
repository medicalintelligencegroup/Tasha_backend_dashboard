package com.mig.patientservice.web.patient.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.neovisionaries.i18n.CountryCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddress {
	@NotNull
	@NotBlank
	private String line1;
	private String line2;
	private String line3;
	@NotNull
	@NotBlank
	private String city;
	private String council;
	@NotNull
	@NotBlank
	private String postcode;
	@NotNull
	private CountryCode country;
}

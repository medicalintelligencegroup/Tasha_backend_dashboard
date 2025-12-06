package com.mig.patientservice.persistence.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MediaReference {
	@NotNull
	@NotBlank
	private String bucket;
	@NotNull
	@NotBlank
	private String key;
}

package com.mig.patientservice.persistence.patient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

package com.mig.patientservice.web.patient.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.mig.patientservice.persistence.patient.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateAnnotation {
	private String id;
	@NotNull
	@NotBlank
	private String patientId;
	@NotNull
	@NotBlank
	private String scanId;
	private Size area;
	private Size averageDepth;

}

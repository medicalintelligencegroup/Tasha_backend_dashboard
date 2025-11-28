package com.mig.patientservice.web.patient.response;

import java.time.Instant;

import com.mig.patientservice.persistence.patient.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnnotationResponse {
	private String id;
	private Instant created;
	private String createdBy;
	private String patientId;
	private String scanId;
	private Size area;
	private Size averageDepth;

}

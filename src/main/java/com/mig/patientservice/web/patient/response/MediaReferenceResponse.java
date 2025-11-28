package com.mig.patientservice.web.patient.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaReferenceResponse {
	private String bucket;
	private String key;
}

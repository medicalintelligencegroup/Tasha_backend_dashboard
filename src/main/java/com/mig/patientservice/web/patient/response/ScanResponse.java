package com.mig.patientservice.web.patient.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanResponse {
	
	private String id;
	private String patientId;
	private Instant created;
	private String createdBy;
	private MediaReferenceResponse ply;
	private MediaReferenceResponse rgb;
	private MediaReferenceResponse rgbd;
	private String patientComments;
	private List<MediaReferenceResponse> images;
	private Double glucoseLiveReadings;
	@JsonProperty("annotations")
	private List<AnnotationResponse> annotationResponseList;

}

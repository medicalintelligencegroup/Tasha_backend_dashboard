package com.mig.patientservice.web.patient.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.mig.patientservice.persistence.patient.MediaReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateScan {
	private String id;
	@NotNull
	@NotBlank
	private String patientId;
	@Valid
	private MediaReference ply;
	@Valid
	private MediaReference rgb;
	@Valid
	private MediaReference rgbd;
	private String patientComments;
	private List<MediaReference> images;
	private Double glucoseLiveReadings;
}

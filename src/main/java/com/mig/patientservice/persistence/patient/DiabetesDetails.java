package com.mig.patientservice.persistence.patient;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiabetesDetails {
	private DiabetesType typeOfDiabetes;
	private Boolean previousAmputation;
	private Long yearsDiagnosedWithDiabetes;
	private String previousFootTreatmentOrDysfunction;
	private String otherDiabeticAssociatedComplications;
	private Long lastHbA1cReading;
	private Instant lastHbA1cDate;
	private Long lastLipidProfile;
	private Instant lastLipidProfileDate;
	private Long latestBp;
	private Instant latestBpDate;
	private Boolean medications;
	private String medicationsInfo;
	private String otherDMorCVComplications;
}

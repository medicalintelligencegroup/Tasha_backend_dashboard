package com.mig.patientservice.web.patient.request;

import java.time.ZonedDateTime;

import com.mig.patientservice.persistence.patient.DiabetesType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDiabetesDetails {
	
	private DiabetesType typeOfDiabetes;
	private Boolean previousAmputation;
	private Long yearsDiagnosedWithDiabetes;
	private String previousFootTreatmentOrDysfunction;
	private String otherDiabeticAssociatedComplications;
	private Long lastHbA1cReading;
	private ZonedDateTime lastHbA1cDate;
	private Long lastLipidProfile;
	private ZonedDateTime lastLipidProfileDate;
	private Long latestBp;
	private ZonedDateTime latestBpDate;
	private Boolean medications;
	private String medicationsInfo;
	private String otherDMorCVComplications;

}

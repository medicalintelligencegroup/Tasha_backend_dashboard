package com.mig.patientservice.web.patient.request;

import java.time.Instant;
import java.time.ZonedDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.mig.patientservice.persistence.patient.PatientStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePatient {
	private String id;
	private Instant created;
	private String createdBy;
	private Instant updated;
	private Long sequenceNumber;
	@Valid
	private CreateAddress address;
	@NotNull
	@NotBlank
	private String firstName;
	@NotNull
	@NotBlank
	private String lastName;
	@NotNull
	@NotBlank
	private String email;
	@NotNull
	@NotBlank
	private String gender;
	private Long height;
	private Long weight;
	private Long bmi;
	@NotNull
	private Long phoneNumber;
	private Instant birthDate;
	private PatientStatus status;
	private String clinicianNotes;
	private ZonedDateTime treatmentStartDate;
	private ZonedDateTime nextVisitDate;
	private ZonedDateTime lastVisitDate;
	private CreateDiabetesDetails diabetesDetails;

}

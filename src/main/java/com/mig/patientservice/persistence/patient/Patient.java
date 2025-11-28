package com.mig.patientservice.persistence.patient;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "patient")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

	@Id
	private String id;
	@CreatedDate
	private Instant created;
	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private Instant updated;
	private Long sequenceNumber;
	private Address address;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private Long height;
	private Long weight;
	private Long bmi;
	private Long phoneNumber;
	private Instant birthDate;
	private PatientStatus status;
	private Instant treatmentStartDate;
	private Instant nextVisitDate;
	private Instant lastVisitDate;
	private List<ClinicianNotes> clinicianNotes;
	private DiabetesDetails diabetesDetails;
	
}

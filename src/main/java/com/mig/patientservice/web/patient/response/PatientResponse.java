package com.mig.patientservice.web.patient.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mig.patientservice.persistence.patient.Address;
import com.mig.patientservice.persistence.patient.ClinicianNotes;
import com.mig.patientservice.persistence.patient.DiabetesDetails;
import com.mig.patientservice.persistence.patient.PatientStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
	private String id;
	private Instant created;
	private String createdBy;
	private Instant updated;
	private Long sequenceNumber;
	private String firstName;
	private String lastName;
	private Address address;
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
	@JsonProperty("scans")
	private List<ScanResponse> scanResponseList;
}

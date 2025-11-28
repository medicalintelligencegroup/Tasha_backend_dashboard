package com.mig.patientservice.persistence.patient;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClinicianNotes {

	private Instant created;
	private String notes;
}

package com.mig.patientservice.persistence.patient;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Size {

	private Double amount;
	private String unit;
}

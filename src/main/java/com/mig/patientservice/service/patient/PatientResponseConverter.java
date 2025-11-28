package com.mig.patientservice.service.patient;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mig.patientservice.persistence.patient.Patient;
import com.mig.patientservice.web.patient.response.PatientResponse;
import org.modelmapper.*;

@Component
public class PatientResponseConverter implements Converter<Patient, PatientResponse> {
	
	private final ModelMapper modelMapper;
	
	public PatientResponseConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public PatientResponse convert(Patient patient) {
		return modelMapper.map(patient, PatientResponse.class);
	}

}

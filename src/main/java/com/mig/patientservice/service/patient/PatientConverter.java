package com.mig.patientservice.service.patient;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mig.patientservice.persistence.patient.Patient;
import com.mig.patientservice.web.patient.request.CreatePatient;

@Component
public class PatientConverter implements Converter<CreatePatient, Patient> {
	
	private final ModelMapper modelMapper;
	
	org.modelmapper.Converter<ZonedDateTime, Instant> toInstantFromZonedDateTime = new AbstractConverter<ZonedDateTime, Instant>() {
        @Override
        protected Instant convert(ZonedDateTime source) {
        	if(Objects.isNull(source))
        		return null;
            Instant instant = source.toInstant();
            return instant;
        }
    };
	
	public PatientConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public Patient convert(CreatePatient createPatient) {
		modelMapper.addConverter(toInstantFromZonedDateTime);
		modelMapper.getTypeMap(ZonedDateTime.class, Instant.class);
		return modelMapper.map(createPatient, Patient.class);
	}

}

package com.mig.patientservice.service.patient;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.web.patient.request.CreateScan;

@Component
public class ScanConverter implements Converter<CreateScan, Scan> {
	
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
	
	public ScanConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public Scan convert(CreateScan createScan) {
		modelMapper.addConverter(toInstantFromZonedDateTime);
		modelMapper.getTypeMap(ZonedDateTime.class, Instant.class);
		return modelMapper.map(createScan, Scan.class);
	}

}

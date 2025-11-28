package com.mig.patientservice.service.patient;

import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.web.patient.response.ScanResponse;

@Component
public class ScanResponseConverter implements Converter<Scan, ScanResponse> {
	
	private final ModelMapper modelMapper;
	
	public ScanResponseConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	public ScanResponse convert(Scan scan) {
		return modelMapper.map(scan, ScanResponse.class);
	}

}

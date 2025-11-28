package com.mig.patientservice.service.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.web.patient.response.ScanResponse;

public interface ScanService {
	
	public Page<Scan> getScanList(Pageable pageable);
	
	public ScanResponse getScan(String scanId);
	
	public Scan createScan(Scan scan);

}

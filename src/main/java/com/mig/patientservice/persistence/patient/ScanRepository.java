package com.mig.patientservice.persistence.patient;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanRepository extends MongoRepository<Scan, String> {

	Page<Scan> findAll(Pageable pageable);
	
	Optional<Scan> findById(String id);
	
	Optional<List<Scan>> findByPatientId(String id);
	
	Scan insert(Scan scan);

}

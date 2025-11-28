package com.mig.patientservice.persistence.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

	Page<Patient> findAll(Pageable pageable);
	
	Patient save(Patient patient);

}

package com.mig.patientservice.persistence.patient;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnotationRepository extends MongoRepository<Annotation, String> {

	Page<Annotation> findAll(Pageable pageable);
	
	Optional<Annotation> findById(String id);
	
	Optional<List<Annotation>> findByScanId(String scanId);
	
	Annotation save(Annotation annotation);

}

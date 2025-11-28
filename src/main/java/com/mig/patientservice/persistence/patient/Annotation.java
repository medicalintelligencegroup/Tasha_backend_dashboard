package com.mig.patientservice.persistence.patient;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "annotation")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
public class Annotation {
	@Id
	private String id;
	@Indexed
	private String scanId;
	@Indexed
	private String patientId;
	@CreatedDate
	private Instant created;
	@CreatedBy
	private String createdBy;
	private Size area;
	private Size averageDepth;
}

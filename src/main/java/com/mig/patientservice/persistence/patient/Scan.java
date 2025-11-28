package com.mig.patientservice.persistence.patient;

import java.time.Instant;
import java.util.List;

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

@Document(collection = "scan")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Scan {
	@Id
	private String id;
	@Indexed
	private String patientId;
	@CreatedDate
	private Instant created;
	@CreatedBy
	private String createdBy;
	private MediaReference ply;
	private MediaReference rgb;
	private MediaReference rgbd;
	private String patientComments;
	private List<MediaReference> images;
	private Double glucoseLiveReadings;
}

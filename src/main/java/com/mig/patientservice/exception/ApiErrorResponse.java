package com.mig.patientservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ApiErrorResponse {

	private HttpStatus status;
	@JsonIgnore
	private LocalDateTime timestamp;
	private String message;
	@JsonIgnore
	private String path;
	@JsonIgnore
	private List<String> subErrors;
	private String appErrorCode;
}

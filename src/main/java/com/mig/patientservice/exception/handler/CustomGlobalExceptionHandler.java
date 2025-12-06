package com.mig.patientservice.exception.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mig.patientservice.exception.AnnotationNotFoundException;
import com.mig.patientservice.exception.ApiErrorInfo;
import com.mig.patientservice.exception.PatientNotFoundException;
import com.mig.patientservice.exception.ScanNotFoundException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@ExceptionHandler({PatientNotFoundException.class, ScanNotFoundException.class, AnnotationNotFoundException.class})
	public final ResponseEntity<Object> handlePatientNotFoundException(Exception exception, WebRequest webRequest) {
		List<String> details = new ArrayList<>();
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.NOT_FOUND,
			LocalDateTime.now(), exception.getMessage(), ((ServletWebRequest) webRequest).getRequest().getRequestURI(), details);
		return buildResponseEntity(apiErrorInfo);
	}

	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<Object> handleConflict(RuntimeException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.BAD_REQUEST,
			LocalDateTime.now(), ex.getMessage(), "", details);
		return buildResponseEntity(apiErrorInfo);
	}
	
	@ExceptionHandler(UnirestException.class)
	public ResponseEntity<Object> handleUnirestException(RuntimeException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.BAD_REQUEST,
			LocalDateTime.now(), ex.getMessage(), "", details);
		return buildResponseEntity(apiErrorInfo);
	}
	
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMostSpecificCause().getLocalizedMessage());
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.BAD_REQUEST,
			LocalDateTime.now(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI(), details);
		return buildResponseEntity(apiErrorInfo);
	}

	protected ResponseEntity<Object> handleConversionNotSupported(HttpMessageConversionException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.BAD_REQUEST,
			LocalDateTime.now(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI(), details);
		return buildResponseEntity(apiErrorInfo);
	}


	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.BAD_REQUEST,
			LocalDateTime.now(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI(), details);

		return buildResponseEntity(apiErrorInfo);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		final ApiErrorInfo apiErrorInfo = new ApiErrorInfo(HttpStatus.BAD_REQUEST,
			LocalDateTime.now(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI(), details);

		return buildResponseEntity(apiErrorInfo);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiErrorInfo apiErrorInfo) {
		return new ResponseEntity<>(apiErrorInfo, apiErrorInfo.getStatus());
	}
}

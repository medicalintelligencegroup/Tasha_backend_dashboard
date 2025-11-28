package com.mig.patientservice.web.patient;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mig.patientservice.persistence.patient.Annotation;
import com.mig.patientservice.service.patient.AnnotationConverter;
import com.mig.patientservice.service.patient.AnnotationResponseConverter;
import com.mig.patientservice.service.patient.AnnotationService;
import com.mig.patientservice.web.patient.request.CreateAnnotation;
import com.mig.patientservice.web.patient.response.AnnotationResponse;
import com.mig.patientservice.web.patient.response.ScanResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "v1/annotations")
@Slf4j
public class AnnotationController {
	
	private final AnnotationService annotationService;
	private final AnnotationResponseConverter annotationResponseConverter;
	private final AnnotationConverter annotationConverter;
	
	public AnnotationController(AnnotationService annotationService, 
			AnnotationResponseConverter annotationResponseConverter,
			AnnotationConverter annotationConverter) {
		this.annotationService = annotationService;
		this.annotationResponseConverter = annotationResponseConverter;
		this.annotationConverter = annotationConverter;
	}
	
		@RequestMapping(
			method = GET,
			produces = APPLICATION_JSON_VALUE)
		//@PreAuthorize("hasAuthority('list:annotations')")
		@ApiOperation(value = "List annotations", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "list:annotations", description = "")
			})
		})
		public ResponseEntity<Page<AnnotationResponse>> listScans(Pageable pageable) {
			Page<AnnotationResponse> annotationListResponse = annotationService.getAnnotationList(pageable).map(annotationResponseConverter::convert);
			
			return ResponseEntity.ok(annotationListResponse);
		}
		
		@RequestMapping(
				path = "/{annotationId}",
				method = GET,
				produces = APPLICATION_JSON_VALUE)
			//@PreAuthorize("hasAuthority('list:patients')")
			@ApiOperation(value = "Get annotation by id", authorizations = {
				@Authorization(value = "auth0", scopes = {
					@AuthorizationScope(scope = "get:annotation", description = "")
				})
			})
			public ResponseEntity<AnnotationResponse> getPatientById(@PathVariable String annotationId) {
				AnnotationResponse annotationResponse = annotationResponseConverter.convert(annotationService.getAnnotation(annotationId));
				return ResponseEntity.ok(annotationResponse);
			}
	
		@RequestMapping(
			method = POST,
			produces = APPLICATION_JSON_VALUE,
			consumes = APPLICATION_JSON_VALUE)
		//@PreAuthorize("hasAuthority('create:annotations')")
		@ApiOperation(value = "Create annotation", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "create:annotation", description = "")
			})
		})
		public ResponseEntity<AnnotationResponse> createAnnotation(@RequestBody CreateAnnotation createAnnotation) {
			Annotation annotation = annotationConverter.convert(createAnnotation);
			Annotation createdAnnotation = annotationService.createAnnotation(annotation);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(annotationResponseConverter.convert(createdAnnotation));
		}

}

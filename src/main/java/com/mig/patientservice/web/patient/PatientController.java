package com.mig.patientservice.web.patient;

import static com.mig.patientservice.web.patient.PatientController.BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.mig.patientservice.persistence.patient.Patient;
import com.mig.patientservice.service.patient.PatientConverter;
import com.mig.patientservice.service.patient.PatientResponseConverter;
import com.mig.patientservice.service.patient.PatientService;
import com.mig.patientservice.web.patient.request.CreatePatient;
import com.mig.patientservice.web.patient.response.PatientResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping(path = BASE_PATH)
@Slf4j
public class PatientController {
	
	static final String BASE_PATH = "/v1/patients";
	private final PatientService patientService;
	private final PatientResponseConverter patientResponseConverter;
	private final PatientConverter patientConverter;
	
	
	public PatientController(PatientService patientService, 
			PatientResponseConverter patientResponseConverter,
			PatientConverter patientConverter) {
		this.patientService = patientService;
		this.patientResponseConverter = patientResponseConverter;
		this.patientConverter = patientConverter;
	}
	
		@RequestMapping(
			method = GET,
			produces = APPLICATION_JSON_VALUE)
		//@PreAuthorize("hasAuthority('SCOPE_list:patients')")
		@ApiOperation(value = "List patients", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "list:patients", description = "")
			})
		})
		public ResponseEntity<Page<PatientResponse>> listPatients(Pageable pageable) {
			Page<PatientResponse> patientListResponse = patientService.getPatientList(pageable).map(patientResponseConverter::convert);
			
			return ResponseEntity.ok(patientListResponse);
		}
		
		@RequestMapping(
				path = "/{patientId}",
				method = GET,
				produces = APPLICATION_JSON_VALUE)
			@PreAuthorize("hasAuthority('SCOPE_get:patient')")
			@ApiOperation(value = "Get patient by id", authorizations = {
				@Authorization(value = "auth0", scopes = {
					@AuthorizationScope(scope = "get:patient", description = "")
				})
			})
			public ResponseEntity<PatientResponse> getPatientById(@PathVariable String patientId) {
				PatientResponse patientResponse = patientService.getPatient(patientId);
				return ResponseEntity.ok(patientResponse);
			}
	
		@RequestMapping(
			method = POST,
			produces = APPLICATION_JSON_VALUE,
			consumes = APPLICATION_JSON_VALUE)
		@PreAuthorize("hasAuthority('SCOPE_create:patient')")
		@ApiOperation(value = "Create patient", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "create:patient", description = "")
			})
		})
		public ResponseEntity<PatientResponse> createPatient(@RequestBody @Valid CreatePatient createPatient) {
			//log.info("Create Patient request, {}", createPatient);
			Patient patient = patientConverter.convert(createPatient);
			//log.info("converted Patient request, {}", patient);
			Patient createdPatient = patientService.createPatient(patient);
			
			return ResponseEntity.created(URI.create(BASE_PATH + "/" + createdPatient.getId())).body(patientResponseConverter.convert(createdPatient));
			
		}
		
		@RequestMapping(
				path = "/{patientId}",
				method = PATCH,
				produces = APPLICATION_JSON_VALUE,
				consumes = APPLICATION_JSON_VALUE)
			@PreAuthorize("hasAuthority('SCOPE_update:patient')")
			@ApiOperation(value = "Update patient", authorizations = {
				@Authorization(value = "auth0", scopes = {
					@AuthorizationScope(scope = "update:patient", description = "")
				})
			})
			public ResponseEntity<PatientResponse> updatePatient(@PathVariable String patientId, @RequestBody @Valid CreatePatient updatePatient) {
				updatePatient.setId(patientId);
				Patient patient = patientConverter.convert(updatePatient);
				//log.info("converted Patient request, {}", patient);
				Patient updatedPatient = patientService.updatePatient(updatePatient, patient);
				
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(patientResponseConverter.convert(updatedPatient));
			}
}

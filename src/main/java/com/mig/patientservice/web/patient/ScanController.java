package com.mig.patientservice.web.patient;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static com.mig.patientservice.web.patient.ScanController.BASE_PATH;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.service.patient.ScanConverter;
import com.mig.patientservice.service.patient.ScanResponseConverter;
import com.mig.patientservice.service.patient.ScanService;
import com.mig.patientservice.web.patient.request.CreateScan;
import com.mig.patientservice.web.patient.response.ScanResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = BASE_PATH)
@Slf4j
public class ScanController {
	
	static final String BASE_PATH = "/v1/scans";
	private final ScanService scanService;
	private final ScanResponseConverter scanResponseConverter;
	private final ScanConverter scanConverter;
	
	public ScanController(ScanService scanService, 
			ScanResponseConverter scanResponseConverter,
			ScanConverter scanConverter) {
		this.scanService = scanService;
		this.scanResponseConverter = scanResponseConverter;
		this.scanConverter = scanConverter;
	}
	
		@RequestMapping(
			method = GET,
			produces = APPLICATION_JSON_VALUE)
		//@PreAuthorize("hasAuthority('list:scans')")
		@ApiOperation(value = "List scans", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "list:scans", description = "")
			})
		})
		public ResponseEntity<Page<ScanResponse>> listScans(Pageable pageable) {
			Page<ScanResponse> scanListResponse = scanService.getScanList(pageable).map(scanResponseConverter::convert);
			
			return ResponseEntity.ok(scanListResponse);
		}
		
		@RequestMapping(
				path = "/{scanId}",
				method = GET,
				produces = APPLICATION_JSON_VALUE)
			//@PreAuthorize("hasAuthority('list:patients')")
			@ApiOperation(value = "Get scan by id", authorizations = {
				@Authorization(value = "auth0", scopes = {
					@AuthorizationScope(scope = "get:scan", description = "")
				})
			})
			public ResponseEntity<ScanResponse> getPatientById(@PathVariable String scanId) {
				ScanResponse scanResponse = scanService.getScan(scanId);
				return ResponseEntity.ok(scanResponse);
			}
	
		@RequestMapping(
			method = POST,
			produces = APPLICATION_JSON_VALUE,
			consumes = APPLICATION_JSON_VALUE)
		//@PreAuthorize("hasAuthority('create:scan')")
		@ApiOperation(value = "Create scan", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "create:scan", description = "")
			})
		})
		public ResponseEntity<ScanResponse> createScan(@RequestBody @Valid CreateScan createScan) {
			Scan scan = scanConverter.convert(createScan);
			Scan createdScan = scanService.createScan(scan);
			
			return ResponseEntity.created(URI.create(BASE_PATH + "/" + createdScan.getId())).body(scanResponseConverter.convert(createdScan));
		}

}

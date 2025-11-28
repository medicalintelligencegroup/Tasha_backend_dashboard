package com.mig.patientservice.web.patient;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mig.patientservice.configuration.SecurityConfiguration;
import com.mig.patientservice.persistence.patient.AnnotationRepository;
import com.mig.patientservice.persistence.patient.PatientRepository;
import com.mig.patientservice.persistence.patient.Scan;
import com.mig.patientservice.persistence.patient.ScanRepository;
import com.mig.patientservice.service.patient.AnnotationService;
import com.mig.patientservice.service.patient.ScanService;
import com.mig.patientservice.web.patient.request.CreateScan;
import com.mig.patientservice.web.patient.response.ScanResponse;


@RunWith(SpringRunner.class)
@WebMvcTest(
	controllers = ScanController.class,
	includeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {
			SecurityConfiguration.class
		}
	)
)
@EnableSpringDataWebSupport
public class ScanControllerFunctionalTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ScanService scanService;
	
	@MockBean
	private AnnotationService annotationService;
	
	@MockBean
	private PatientRepository patientRepository;
	
	@MockBean
	private ScanRepository scanRepository;
	
	@MockBean
	private AnnotationRepository annotationRepository;


	@Before
	public void setUp() {
		
	}

	@Test
	@WithMockUser(value = "auth0|james", authorities = "create:patient")
	public void shouldReturn201WhenScanCreatedWithValidData() throws Exception {
		
		CreateScan scanBody = CreateScan.builder()
									.patientId("1")
									.build();

		given(scanService.createScan(any())).willReturn(Scan.builder().id("1").build());

		mvc.perform(post("/v1/scans")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(scanBody)))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/v1/scans/1"));
	}
	
	
	@Test
	@WithMockUser(value = "auth0|james", authorities = "update:patient")
	public void shouldReturn200WhenRetrievingScanData() throws Exception {

		given(scanService.getScan(any())).willReturn(ScanResponse.builder().id("2").build());

		mvc.perform(get("/v1/scans/2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

}

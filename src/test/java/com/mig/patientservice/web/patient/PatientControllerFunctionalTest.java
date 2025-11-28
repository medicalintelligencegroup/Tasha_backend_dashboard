package com.mig.patientservice.web.patient;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.mig.patientservice.persistence.patient.Patient;
import com.mig.patientservice.persistence.patient.PatientRepository;
import com.mig.patientservice.persistence.patient.ScanRepository;
import com.mig.patientservice.service.patient.AnnotationService;
import com.mig.patientservice.service.patient.PatientService;
import com.mig.patientservice.service.patient.ScanService;
import com.mig.patientservice.web.patient.request.CreateAddress;
import com.mig.patientservice.web.patient.request.CreatePatient;
import com.mig.patientservice.web.patient.response.PatientResponse;
import com.neovisionaries.i18n.CountryCode;


@RunWith(SpringRunner.class)
@WebMvcTest(
	controllers = PatientController.class,
	includeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {
			SecurityConfiguration.class
		}
	)
)
@EnableSpringDataWebSupport
public class PatientControllerFunctionalTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PatientService patientService;
	
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
	public void shouldReturn201WhenPatientCreatedWithValidData() throws Exception {
		
		CreateAddress address = new CreateAddress("line 1", "woo hoo", "allo monsiour", "paris", "ile de france", "BH10 2AA", CountryCode.GB);
		
		CreatePatient patientBody = CreatePatient.builder()
									.id("1")
									.firstName("Roger")
									.lastName("Federer")
									.gender("M")
									.address(address)
									.email("test@test.com")
									.phoneNumber(Long.valueOf("0123456789"))
									.build();

		given(patientService.createPatient(any())).willReturn(Patient.builder().id("1").build());

		mvc.perform(post("/v1/patients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientBody)))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/v1/patients/1"));
	}
	
	@Test
	@WithMockUser(value = "auth0|james", authorities = "update:patient")
	public void shouldReturn204WhenPatientUpdatedWithValidData() throws Exception {
		
		CreateAddress address = new CreateAddress("line 1", "woo hoo", "allo monsiour", "paris", "ile de france", "BH10 2AA", CountryCode.GB);
		
		CreatePatient patientBody = CreatePatient.builder()
									.id("2")
									.firstName("Roger")
									.lastName("Federer")
									.gender("M")
									.address(address)
									.email("test@test.com")
									.phoneNumber(Long.valueOf("0123456789"))
									.build();

		given(patientService.updatePatient(any(), any())).willReturn(Patient.builder().id("2").build());

		mvc.perform(patch("/v1/patients/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientBody)))
			.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value = "auth0|james", authorities = "update:patient")
	public void shouldReturn200WhenRetrievingPatientData() throws Exception {

		given(patientService.getPatient(any())).willReturn(PatientResponse.builder().id("2").build());

		mvc.perform(get("/v1/patients/2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

}

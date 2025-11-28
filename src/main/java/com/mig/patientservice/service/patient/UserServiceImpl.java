package com.mig.patientservice.service.patient;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mig.patientservice.http.Auth0Service;
import com.mig.patientservice.web.patient.request.User;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	
	private Auth0Service auth0Service;
	
	private final RestTemplate restTemplate;
	
	public UserServiceImpl(Auth0Service auth0Service,
			RestTemplateBuilder restTemplateBuilder) {
		this.auth0Service = auth0Service;
		this.restTemplate = restTemplateBuilder.rootUri("https://mig-test.uk.auth0.com").build();
	}
	
	public void updateUserMetadata(User user) throws Exception {
		
		log.info("User role: {}", user.getJobRole());
		log.info("Mngt token: {}", auth0Service.getManagementApiToken());
		
		/* This code seems to fail after 2 successful trnasactions. Issue with probably the Unitest lib
		HttpResponse<String> response = Unirest.patch(String.format("https://mig-test.uk.auth0.com/api/v2/users/%s",
				URLEncoder.encode(SecurityContextHolder.getContext().getAuthentication().getName(),"UTF-8")))
				  .header("authorization", "Bearer " + auth0Service.getManagementApiToken())
				  .header("content-type", "application/json")
				  .body("{\"user_metadata\":{\"job_role\":\"" + user.getJobRole() + "\"}}")
				  .asString();
		*/
		
		UserMetadata userMetadata = UserMetadata.builder()
				.jobRole(user.getJobRole()).build();
		UserPatchEntity userPatchEntity = UserPatchEntity.builder()
				.userMetadata(userMetadata).build();
		//log.info("User patch entity {}", new ObjectMapper().writeValueAsString(userPatchEntity));
		
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://mig-test.uk.auth0.com/api/v2/users/%s",
        				URLEncoder.encode(SecurityContextHolder.getContext().getAuthentication().getName(),"UTF-8"))))
                .header("Content-Type", "application/json")
                .header("authorization", "Bearer " + auth0Service.getManagementApiToken())
                .method("PATCH", HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(userPatchEntity)))
                .build();
			HttpResponse<String> response = HttpClient.newHttpClient()
			        .send(request, HttpResponse.BodyHandlers.ofString());
		log.info("User metadata update complete");	  
				 
	}
 
}

@Data
@Builder
class UserPatchEntity {
	@JsonProperty("user_metadata")
	UserMetadata userMetadata;
}

@Data
@Builder
class UserMetadata {
	@JsonProperty("job_role")
	private String jobRole;
}

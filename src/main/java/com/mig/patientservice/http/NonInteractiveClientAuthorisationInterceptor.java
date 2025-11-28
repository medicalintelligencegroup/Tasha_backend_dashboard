package com.mig.patientservice.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class NonInteractiveClientAuthorisationInterceptor implements ClientHttpRequestInterceptor {

	private final Auth0Service auth0Service;

	@Autowired
	public NonInteractiveClientAuthorisationInterceptor(Auth0Service auth0Service) {
		this.auth0Service = auth0Service;
	}

	@Override
	@Nonnull
	public ClientHttpResponse intercept(@Nonnull HttpRequest request, @Nonnull byte[] body, @Nonnull ClientHttpRequestExecution execution) throws IOException {
		request.getHeaders().add(AUTHORIZATION, "Bearer " + auth0Service.getAccessToken());
		return execution.execute(request, body);
	}
}

package com.mig.patientservice.configuration;

import com.auth0.client.auth.AuthAPI;
import com.mig.patientservice.http.NonInteractiveClientAuthorisationInterceptor;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
public class AuthApiConfiguration {

	@Bean
	public AuthAPI auth0Api(Auth0ConfigurationProperties auth0ConfigurationProperties) {
		return new AuthAPI(auth0ConfigurationProperties.getDomain(),
			auth0ConfigurationProperties.getClientId(),
			auth0ConfigurationProperties.getClientSecret());
	}

	/**
	 * The default {@link RestTemplateBuilder} is configured with a non interactive client access token
	 * If you need to pass the logged in users token, you will need to create another {@link RestTemplateBuilder} with a different {@link ClientHttpRequestInterceptor}
	 *
	 * @param nonInteractiveClientAuthorisationInterceptor A {@link ClientHttpRequestInterceptor} that can add access tokens to outgoing requests
	 * @return The configured rest template builder
	 */
	@Bean
	public RestTemplateBuilder restTemplateBuilder(NonInteractiveClientAuthorisationInterceptor nonInteractiveClientAuthorisationInterceptor) {
		return new RestTemplateBuilder().additionalInterceptors(nonInteractiveClientAuthorisationInterceptor);
	}
	
	@Bean
	  public ModelMapper modelMapper() {
	    return new ModelMapper();
	  }
}

package com.mig.patientservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "auth0")
@Validated
@Data
public class Auth0ConfigurationProperties {
	@NotNull
	private String apiAudience;
	@NotNull
	private String managementApiAudience;
	@NotNull
	private String domain;
	@NotNull
	private String clientId;
	@NotNull
	private String clientSecret;

	public String getIssuer() {
		return String.format("https://%s/", this.domain);
	}
}

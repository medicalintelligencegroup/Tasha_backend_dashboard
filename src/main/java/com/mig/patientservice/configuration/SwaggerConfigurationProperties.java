package com.mig.patientservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "swagger")
@Validated
public class SwaggerConfigurationProperties {
	@NotNull
	private String host;

	@Valid
	private Auth0 auth0;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Auth0 getAuth0() {
		return auth0;
	}

	public void setAuth0(Auth0 auth0) {
		this.auth0 = auth0;
	}

	public static class Auth0 {
		@NotNull
		private String clientId;
		@NotNull
		private String clientSecret;
		@NotNull
		private String apiAudience;
		@NotNull
		private String domain;

		public String getClientId() {
			return clientId;
		}

		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		public String getClientSecret() {
			return clientSecret;
		}

		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}

		public String getApiAudience() {
			return apiAudience;
		}

		public void setApiAudience(String apiAudience) {
			this.apiAudience = apiAudience;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}
	}
}

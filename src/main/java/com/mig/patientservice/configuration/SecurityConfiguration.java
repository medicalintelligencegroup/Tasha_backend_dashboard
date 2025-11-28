package com.mig.patientservice.configuration;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;

@Configuration
@EnableConfigurationProperties(Auth0ConfigurationProperties.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final Auth0ConfigurationProperties auth0ConfigurationProperties;

	public SecurityConfiguration(Auth0ConfigurationProperties auth0ConfigurationProperties) {
		this.auth0ConfigurationProperties = auth0ConfigurationProperties;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.requestMatchers(
				EndpointRequest.to("health", "info"),
				PathRequest.toStaticResources().atCommonLocations())
			.antMatchers("/swagger-ui.html", "/v2/api-docs/**", "/swagger-resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtWebSecurityConfigurer
			.forRS256(auth0ConfigurationProperties.getApiAudience(), auth0ConfigurationProperties.getIssuer())
			.configure(http)
			.cors().and()
			.authorizeRequests().anyRequest().authenticated();
	}
	
}

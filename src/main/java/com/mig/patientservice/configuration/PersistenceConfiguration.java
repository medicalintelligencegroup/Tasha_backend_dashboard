package com.mig.patientservice.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;

@Configuration
@EntityScan(basePackages = {"com.mig.patientservice.persistence"})
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class PersistenceConfiguration {

	@Bean
	public DateTimeProvider dateTimeProvider() {
		return () -> Optional.of(Instant.now());
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null ||
				authentication instanceof AnonymousAuthenticationToken ||
				!authentication.isAuthenticated()) {
				return Optional.empty();
			}

			return Optional.ofNullable(authentication.getName());
		};
	}
}

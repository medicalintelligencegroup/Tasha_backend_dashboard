package com.mig.patientservice.http;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.mig.patientservice.configuration.Auth0ConfigurationProperties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.Instant.now;
import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class Auth0Service {
	private static final Logger LOGGER = LoggerFactory.getLogger(Auth0Service.class);
	public final String DIGITAL_SURVEYOR = "Digital Surveyor";
	public final String DEVELOPER = "Developer";
	private final AuthAPI authAPI;
	private final String apiAudience;
	private final String apiDomain;
	private final String managementApiAudience;

	private ExpirebleToken expirebleToken;
	private ExpirebleToken managementApiExpirableToken;

	@Autowired
	Auth0Service(AuthAPI authAPI, Auth0ConfigurationProperties auth0ConfigurationProperties) {
		this.authAPI = authAPI;
		this.apiAudience = auth0ConfigurationProperties.getApiAudience();
		this.managementApiAudience = auth0ConfigurationProperties.getManagementApiAudience();
		this.apiDomain = auth0ConfigurationProperties.getDomain();
	}

	public synchronized String getAccessToken() {
		if (expirebleToken == null || expirebleToken.isExpired()) {
			try {
				expirebleToken = new ExpirebleToken(authAPI.requestToken(apiAudience).execute().getBody());
			} catch (Auth0Exception e) {
				LOGGER.error("Error getting Auth0 token", e);
				expirebleToken = null;
			}
		}

		return ofNullable(expirebleToken).orElse(new ExpirebleToken()).getAccessToken();
	}

	public synchronized String getManagementApiToken() {
		//if (managementApiExpirableToken == null || managementApiExpirableToken.isExpired()) {
			try {
				managementApiExpirableToken = new ExpirebleToken(authAPI.requestToken(managementApiAudience).execute().getBody());
			} catch (Auth0Exception e) {
				LOGGER.error("Error getting management Auth0 token", e);
				managementApiExpirableToken = null;
			}
		//}

		return ofNullable(managementApiExpirableToken).orElse(new ExpirebleToken()).getAccessToken();
	}

	public User getUser(String id) {
		try {
			return new ManagementAPI(apiDomain, getManagementApiToken())
				.users()
				.get(id, null)
				.execute().getBody();
		} catch (Auth0Exception e) {
			LOGGER.error("Error getting Auth0 user", e);
			return null;
		}
	}

	public UsersPage getDigitalSurveyors(int pageNumber,
										 int amountPerPage,
										 List<String> roleNamesToInclude,
										 List<String> roleNamesToExclude,
										 boolean epcCheck,
										 boolean excludeDSsOnlyToBeAssignedToVacantProperties) {
		try {
			List<String> queryStrings = new ArrayList<>();
			String notBlockedQuery = "NOT blocked:true";
			String employeeQuery = "app_metadata.pupil.userType:\"EMPLOYEE\"";

			queryStrings.add(notBlockedQuery);
			queryStrings.add(employeeQuery);
			if (roleNamesToInclude.size() > 0) {
				String roleNamesToIncludeQuery = (roleNamesToInclude.size() > 1 ? "(" : "")
					+ roleNamesToInclude.stream().map(roleNameToInclude -> "(app_metadata.pupil.roles:\"" + roleNameToInclude + "\")").collect(Collectors.joining("OR"))
					+ (roleNamesToInclude.size() > 1 ? ")" : "");
				queryStrings.add(roleNamesToIncludeQuery);
			}
			if (roleNamesToExclude.size() > 0) {
				String roleNamesToExcludeQuery = "NOT" + (roleNamesToExclude.size() > 1 ? " (" : " ")
					+ roleNamesToExclude.stream().map(roleNameToExclude -> "(app_metadata.pupil.roles:\"" + roleNameToExclude + "\")").collect(Collectors.joining("OR"))
					+ (roleNamesToExclude.size() > 1 ? ")" : "");
				queryStrings.add(roleNamesToExcludeQuery);
			}
			if (epcCheck) {
				String epcCheckToIncludeQuery = "(user_metadata.skills.name:\"EPC\")";
				queryStrings.add(epcCheckToIncludeQuery);
			}
			if (excludeDSsOnlyToBeAssignedToVacantProperties) {
				String excludeDSsOnlyBeAssignedToVacantPropertiesQuery = "(NOT user_metadata.onlyAssignToVacantProperty:\"true\")";
				queryStrings.add(excludeDSsOnlyBeAssignedToVacantPropertiesQuery);
			}

			String query = String.join(" AND ", queryStrings);

			log.info("Auth User query: " + query);

			UserFilter userFilter = new UserFilter()
				.withSearchEngine("v3") // "v2" is deprecated
				.withPage(pageNumber, amountPerPage) // 100 is maximum // TODO this need to be refactored to 50 by January 2021
				.withTotals(true)
				.withSort("nickname:1") // nickname is closest to full name order. given_name, family_name, and full_name doesn't sort users as expected
				.withQuery(query);

			return new ManagementAPI(apiDomain, getManagementApiToken())
				.users()
				.list(userFilter)
				.execute().getBody();
		} catch (Auth0Exception e) {
			LOGGER.error("Error getting DigitalSurveyors from Auth0", e);
			return null;
		}
	}

	public Optional<String> getDSTelephoneNumber(final String digitalSurveyorId) {
		final User digitalSurveyor = getUser(digitalSurveyorId);
		Optional<String> telephoneNumber = Optional.empty();
		if (Objects.nonNull(digitalSurveyor)) {
			final Map<String, Object> userMetaData = digitalSurveyor.getUserMetadata();
			if (userMetaData.containsKey("telephone_number")) {
				final String phoneNumber = (String) userMetaData.get("telephone_number");
				telephoneNumber = cleanseTelephoneNumber(phoneNumber);
			}
		}
		return telephoneNumber;
	}

	private Optional<String> cleanseTelephoneNumber(final String telephoneNumber) {
		return StringUtils.isNotBlank(telephoneNumber) ? Optional.of(telephoneNumber.trim()) : Optional.empty();
	}

	public String getDSPostCode(String digitalSurveyorId) {

		User digitalSurveyor = getUser(digitalSurveyorId);
		if (digitalSurveyor == null) {
			return null;
		}

		Map<String, Object> userMetaData = digitalSurveyor.getUserMetadata();
		if (userMetaData.containsKey("address")) {
			Map<String, Object> obj = (Map<String, Object>) userMetaData.get("address");
			return cleansePostcode(((String) obj.get("postcode")));
		}
		return null;
	}

	public String cleansePostcode(String postcode) {
		return StringUtils.isNotBlank(postcode) ? postcode.trim() : null;
	}

	static class ExpirebleToken {

		private Instant expiration;

		private String accessToken;

		ExpirebleToken() {
		}

		ExpirebleToken(TokenHolder auth0Token) {
			this.expiration = now().plusSeconds(auth0Token.getExpiresIn());
			this.accessToken = auth0Token.getAccessToken();
		}

		boolean isExpired() {
			return now().isAfter(expiration);
		}

		String getAccessToken() {
			return accessToken;
		}
	}
}

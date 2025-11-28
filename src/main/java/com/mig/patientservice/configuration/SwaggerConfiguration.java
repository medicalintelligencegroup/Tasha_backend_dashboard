package com.mig.patientservice.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.builders.TokenEndpointBuilder;
import springfox.documentation.builders.TokenRequestEndpointBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@Profile({"api-docs","dev"})
@EnableSwagger2
@Import({BeanValidatorPluginsConfiguration.class})
@EnableConfigurationProperties(SwaggerConfigurationProperties.class)
public class SwaggerConfiguration {

	private final SwaggerConfigurationProperties swaggerConfigurationProperties;

	@Autowired
	public SwaggerConfiguration(SwaggerConfigurationProperties swaggerConfigurationProperties) {
		this.swaggerConfigurationProperties = swaggerConfigurationProperties;
	}

	@Bean
	public Docket patientApi() {
		return new Docket(SWAGGER_2)
			.host(swaggerConfigurationProperties.getHost())
			.select()
			.apis(input -> input != null && input.isAnnotatedWith(ApiOperation.class))
			.build()
			.ignoredParameterTypes(Authentication.class)
			.forCodeGeneration(true)
			.securitySchemes(securitySchemes());
	}

	@Bean
	public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
		return new AlternateTypeRuleConvention() {
			@Override
			public int getOrder() {
				return Ordered.HIGHEST_PRECEDENCE;
			}

			@Override
			public List<AlternateTypeRule> rules() {
				return newArrayList(
					newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin()))
				);
			}
		};
	}

	@Bean
	public springfox.documentation.swagger.web.SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder()
			.clientId(swaggerConfigurationProperties.getAuth0().getClientId())
			.clientSecret(swaggerConfigurationProperties.getAuth0().getClientSecret())
			.additionalQueryStringParams(ImmutableMap.of("audience", swaggerConfigurationProperties.getAuth0().getApiAudience()))
			.build();
	}

	private List<SecurityScheme> securitySchemes() {
		return ImmutableList.<SecurityScheme>builder()
			.add(new OAuth("auth0", authorizationScopes(), grantTypes()))
			.build();
	}

	private List<AuthorizationScope> authorizationScopes() {
		return ImmutableList.<AuthorizationScope>builder()
			.add(new AuthorizationScope("create:patient", "Create a patient"))
			.add(new AuthorizationScope("get:patient", "Get a single patient"))
			.add(new AuthorizationScope("list:patients", "List patients"))
			.add(new AuthorizationScope("update:patient", "Update patient details"))
			.add(new AuthorizationScope("publish:order", "Publish an Order"))
			.add(new AuthorizationScope("create:scan", "Create a patient scan"))
			.add(new AuthorizationScope("list:scans", "List patient scans"))
			.add(new AuthorizationScope("create:scanAnnotation", "Create annotations for a patient scan"))
			.add(new AuthorizationScope("list:scans", "List annotations of a patient scan"))
			.build();
	}

	private List<GrantType> grantTypes() {
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpointBuilder()
			.url(String.format("https://%s/authorize", swaggerConfigurationProperties.getAuth0().getDomain()))
			.build();
		TokenEndpoint tokenEndpoint = new TokenEndpointBuilder()
			.url(String.format("https://%s/oauth/token", swaggerConfigurationProperties.getAuth0().getDomain()))
			.build();
		return ImmutableList.<GrantType>builder()
			.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint))
			.build();
	}

	private Type pageableMixin() {
		return new AlternateTypeBuilder()
			.fullyQualifiedClassName(
				String.format("%s.generated.%s",
					Pageable.class.getPackage().getName(),
					Pageable.class.getSimpleName()))
			.withProperties(newArrayList(
				property(Integer.class, "page"),
				property(Integer.class, "size"),
				property(String.class, "sort")
			))
			.build();
	}

	private AlternateTypePropertyBuilder property(Class<?> type, String name) {
		return new AlternateTypePropertyBuilder()
			.withName(name)
			.withType(type)
			.withCanRead(true)
			.withCanWrite(true);
	}
}

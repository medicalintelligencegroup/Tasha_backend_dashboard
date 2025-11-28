package com.mig.patientservice.configuration;

import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;	

@Configuration
@Profile("aws-messaging")
public class SpringCloudAwsConfiguration {

	/**
	 * Create a message converter that can convert using our pre-configured object mapper
	 *
	 * @param objectMapper The spring configured {@link ObjectMapper}
	 * @return A configured {@link MappingJackson2MessageConverter}
	 */
	@Bean
	public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
		MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
		mappingJackson2MessageConverter.setObjectMapper(objectMapper);
		mappingJackson2MessageConverter.setSerializedPayloadClass(String.class);
		mappingJackson2MessageConverter.setStrictContentTypeMatch(false);
		return mappingJackson2MessageConverter;
	}

	/**
	 * Create a {@link NotificationMessagingTemplate} that can send SNS messages using a {@link MappingJackson2MessageConverter}
	 *
	 * @param amazonSNS                       The default {@link AmazonSNS} client
	 * @param mappingJackson2MessageConverter The configured {@link MappingJackson2MessageConverter}
	 * @return A configured {@link  NotificationMessagingTemplate}
	 */
	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS, MappingJackson2MessageConverter mappingJackson2MessageConverter) {
		return new NotificationMessagingTemplate(amazonSNS, (ResourceIdResolver) null, mappingJackson2MessageConverter);
	}

	/**
	 * Create a {@link QueueMessagingTemplate} that can send SQS messages using a {@link MappingJackson2MessageConverter}
	 *
	 * @param amazonSQS                       The default {@link AmazonSNSAsync} client
	 * @param mappingJackson2MessageConverter The configured {@link MappingJackson2MessageConverter}
	 * @return A configured {@link QueueMessagingTemplate}
	 */
	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQS, MappingJackson2MessageConverter mappingJackson2MessageConverter) {
		return new QueueMessagingTemplate(amazonSQS, (ResourceIdResolver) null, mappingJackson2MessageConverter);
	}

	/**
	 * Create a {@link SimpleMessageListenerContainerFactory} that can scan for {@link SqsListener} methods and receive SQS messages
	 *
	 * @param amazonSQS The default {@link AmazonSNSAsync} client
	 * @return A configured and auto-started {@link SimpleMessageListenerContainerFactory}
	 */
	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQS) {
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		factory.setAmazonSqs(amazonSQS);
		factory.setAutoStartup(true);
		factory.setMaxNumberOfMessages(10);
		factory.setWaitTimeOut(20);
		return factory;
	}
}

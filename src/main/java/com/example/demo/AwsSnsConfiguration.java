package com.example.demo;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

import com.amazonaws.services.sqs.AmazonSQSAsync;

@Configuration
@ImportResource(locations = { "classpath:/application-context.xml" })
public class AwsSnsConfiguration {
	
	@Autowired
	private AmazonSQSAsync amazonSQSAsync;
	
	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory() {
		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		factory.setAmazonSqs(amazonSQSAsync);
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setStrictContentTypeMatch(false);
		factory.setArgumentResolvers(Collections.<HandlerMethodArgumentResolver>singletonList(new PayloadArgumentResolver(converter)));
		return factory;
	}

	
	@Bean
	public QueueMessagingTemplate messagingTemplate() {
		return new QueueMessagingTemplate(amazonSQSAsync);
	}
	
	/*
	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageContainerFactory() {
		SimpleMessageListenerContainerFactory msgListenerContainer = new SimpleMessageListenerContainerFactory();
		msgListenerContainer.setAmazonSqs(getSQSClientAsyn());
		msgListenerContainer.setAutoStartup(true);
		msgListenerContainer.setMaxNumberOfMessages(5);
	//	msgListenerContainer.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return msgListenerContainer;
	}
*/
}

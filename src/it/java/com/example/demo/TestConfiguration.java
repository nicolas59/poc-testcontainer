package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 * Used to obtain aws sqs client from localstack.
 * 
 * @author root
 *
 */
@Configuration
public class TestConfiguration {

	@Bean
	public AmazonSQS getSQSClient() {
		return AmazonSQSClientBuilder.standard()
				.withEndpointConfiguration(ITDemoApplicationTests.localstack.getEndpointConfiguration(Service.SQS))
				.withCredentials(ITDemoApplicationTests.localstack.getDefaultCredentialsProvider()).build();
	}

	@Bean
	@Primary
	public AmazonSQSAsync getSQSClientAsyn() {
		return AmazonSQSAsyncClientBuilder.standard()
				.withEndpointConfiguration(ITDemoApplicationTests.localstack.getEndpointConfiguration(Service.SQS))
				.withCredentials(ITDemoApplicationTests.localstack.getDefaultCredentialsProvider()).build();
	}

	// public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsyncClient )

	@Primary
	@Bean
	public AWSCredentialsProvider getChain() {
		return ITDemoApplicationTests.localstack.getDefaultCredentialsProvider();
	}

	

}

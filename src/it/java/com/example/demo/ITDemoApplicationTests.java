package com.example.demo;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(initializers = { ITDemoApplicationTests.Intializer.class })
public class ITDemoApplicationTests {

	@Autowired
	TodoRepository todoRepository;

	@ClassRule
	public static PostgreSQLContainer postgreContainer = new PostgreSQLContainer().withDatabaseName("testcontainer")
			.withUsername("sa").withPassword("");

	@ClassRule
	public static LocalStackContainer localstack = new LocalStackContainer().withServices(Service.SQS);

	static class Intializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertyValues.of("spring.datasource.url=" + postgreContainer.getJdbcUrl())
					.applyTo(applicationContext.getEnvironment());

		}

	}

	private static AmazonSQS client;

	@Autowired
	private QueueMessagingTemplate queueTemplatee;

	@BeforeClass
	public static void setup() {
		client = AmazonSQSClientBuilder.standard()
				.withEndpointConfiguration(localstack.getEndpointConfiguration(Service.SQS))
				.withCredentials(localstack.getDefaultCredentialsProvider()).build();

		client.createQueue("test-queue-localstack");
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void should_get_todo() throws Exception {
		var todo = new Todo();
		todo.label = "Tester localstack";

		queueTemplatee.convertAndSend("test-queue-localstack", todo);
		queueTemplatee.convertAndSend("test-queue-localstack", todo);
		Thread.sleep(5000);
		
			
		Optional<Todo> todos = this.todoRepository.findById(1L);
		assertEquals(true, todos.isPresent());

		assertEquals(2, this.todoRepository.count());

	}

}

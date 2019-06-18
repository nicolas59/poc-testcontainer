package com.example.demo;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(initializers ={DemoApplicationTests.Intializer.class})
public class DemoApplicationTests {

	@Autowired
	TodoRepository todoRepository;

	@ClassRule
	public static PostgreSQLContainer postgreContainer = new PostgreSQLContainer().withDatabaseName("testcontainer")
			.withUsername("sa").withPassword("");

	static class Intializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertyValues.of("spring.datasource.url=" + postgreContainer.getJdbcUrl())
					.applyTo(applicationContext.getEnvironment());

		}

	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void should_get_todo() {
		Optional<Todo> todos = this.todoRepository.findById(1L);
		assertEquals(true, todos.isPresent());

	}

}

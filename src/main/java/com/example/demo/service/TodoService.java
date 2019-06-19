package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoRepository;

@Component
public class TodoService {

	@Autowired
	private TodoRepository todoRepository;
	
	@SqsListener(value = "test-queue-localstack", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	@Transactional
	public void receiveTodo(Todo todo) {
		this.todoRepository.save(todo);
	}
}

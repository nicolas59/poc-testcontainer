package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {

}

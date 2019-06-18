package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Todo {

	@Id
	Long id;
	String label;
	boolean completed;
}

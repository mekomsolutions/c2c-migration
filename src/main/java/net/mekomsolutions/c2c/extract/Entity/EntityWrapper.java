package net.mekomsolutions.c2c.extract.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityWrapper<T> {

	@JsonProperty("class")
	private String modelClass;
	
	@JsonProperty("payload")
	private T entity;

	public EntityWrapper(String modelClass, T entity) {
		super();
		this.modelClass = modelClass;
		this.entity = entity;
	}
	
}

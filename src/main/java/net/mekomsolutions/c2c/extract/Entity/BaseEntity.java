package net.mekomsolutions.c2c.extract.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseEntity {

	@JsonIgnore
	private String modelClassName;

	@JsonProperty
	private String uuid;

	public BaseEntity(String modelClassName, String uuid) {
		this.modelClassName = modelClassName;
		this.uuid = uuid;
	}
	
	public String getModelClassName() {
		return modelClassName;
	}

	public String getUuid() {
		return uuid;
	}
}

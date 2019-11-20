package net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenMRSEntity {

	@JsonIgnore
	private String modelClassName;

	@JsonProperty
	private String uuid;
	
	public OpenMRSEntity(String modelClassName,String uuid) {
		super();
		this.uuid = uuid;
		this.modelClassName = modelClassName;
	}

	public String getUuid() {
		return uuid;
	}

	public String getModelClassName() {
		return modelClassName;
	}

}

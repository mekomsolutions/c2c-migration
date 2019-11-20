package net.mekomsolutions.c2c.extract.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncEntity;

public class EntityWrapper<T extends SyncEntity> {

	@JsonProperty("tableToSyncModelClass")
	private String modelClass;
	
	@JsonIgnore
	private String uuid;
	
	@JsonProperty("model")
	private T entity;

	public EntityWrapper(String modelClass, String uuid, T entity) {
		super();
		this.modelClass = modelClass;
		this.uuid = uuid;
		this.entity = entity;
	}
	
	public EntityWrapper(T entity) {
		this.entity = entity;
		try {
			entity.getModelClassName();
			this.modelClass = entity.getModelClassName();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			entity.getUuid();
			this.uuid = entity.getUuid();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getModelClass() {
		return modelClass;
	}
	
	public String getUuid() {
		return uuid;
	}

	public T getEntity() {
		return entity;
	}

}

package net.mekomsolutions.c2c.extract.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPatient;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonName;

/**
 * Describes a Patient, which is merely a {@link java.util.List list}
 * of {@link net.mekomsolutions.c2c.extract.entity.EntityWrapper wrapped entities}
 * of type {@link net.mekomsolutions.c2c.extract.entity.sync.SyncEntity SyncEntity}.
 */
public class Patient {

	@JsonProperty
	private List<EntityWrapper<SyncEntity>> entities;

	public Patient(List<SyncEntity> syncEntities) {
		
		List<EntityWrapper<SyncEntity>> asWrappedEntities = new ArrayList<EntityWrapper<SyncEntity>>() ;
		
		for (SyncEntity entity: syncEntities) {
			asWrappedEntities.add(new EntityWrapper<SyncEntity>(entity));
		}
		
		this.entities = asWrappedEntities;
	}
	
	public List<EntityWrapper<SyncEntity>> getEntities() {
		return entities;
	}

}

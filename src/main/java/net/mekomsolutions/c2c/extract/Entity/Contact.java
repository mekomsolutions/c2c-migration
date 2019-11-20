package net.mekomsolutions.c2c.extract.Entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncEntity;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncPerson;

public class Contact {

	@JsonProperty
	private List<EntityWrapper<SyncEntity>> entities;
	
	public Contact(SyncPerson SyncPerson) {
		this.entities = Arrays.asList(new EntityWrapper<SyncEntity>(SyncPerson));
	}

	public List<EntityWrapper<SyncEntity>> getEntities() {
		return entities;
	}
	
}

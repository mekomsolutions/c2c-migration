package net.mekomsolutions.c2c.extract.Entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSEntity;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPerson;

public class Contact {

	@JsonProperty
	private List<EntityWrapper<OpenMRSEntity>> entities;
	
	public Contact(OpenMRSPerson openMRSPerson) {
		this.entities = Arrays.asList(new EntityWrapper<OpenMRSEntity>(openMRSPerson));
	}

	public List<EntityWrapper<OpenMRSEntity>> getEntities() {
		return entities;
	}
	
}

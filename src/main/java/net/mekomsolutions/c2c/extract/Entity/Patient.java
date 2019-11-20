package net.mekomsolutions.c2c.extract.Entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSEntity;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPatient;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPerson;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.OpenMRSPersonName;

public class Patient {

	@JsonProperty
	private List<EntityWrapper<OpenMRSEntity>> entities;
	
	public Patient(OpenMRSPatient openMRSPatient, OpenMRSPerson openMRSPerson, OpenMRSPersonName openMRSPersonName) {
		this.entities = Arrays.asList(new EntityWrapper<OpenMRSEntity>(openMRSPatient), 
				new EntityWrapper<OpenMRSEntity>(openMRSPerson), 
				new EntityWrapper<OpenMRSEntity>(openMRSPersonName));
	}

	public List<EntityWrapper<OpenMRSEntity>> getEntities() {
		return entities;
	}
	
}

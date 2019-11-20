package net.mekomsolutions.c2c.extract.Entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncEntity;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncPatient;
import net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity.SyncPersonName;

public class Patient {

	@JsonProperty
	private List<EntityWrapper<SyncEntity>> entities;

	public Patient(SyncPatient openMRSPatient, SyncPersonName openMRSPersonName) {
		this.entities = Arrays.asList(
				new EntityWrapper<SyncEntity>(openMRSPatient), 
				new EntityWrapper<SyncEntity>(openMRSPersonName)
				);
	}

	public List<EntityWrapper<SyncEntity>> getEntities() {
		return entities;
	}

}

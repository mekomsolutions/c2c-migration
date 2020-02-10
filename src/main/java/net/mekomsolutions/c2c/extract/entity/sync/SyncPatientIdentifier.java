package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.UUID;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;

public class SyncPatientIdentifier extends SyncEntity {

	@JsonProperty
	private String identifier;

	@JsonProperty("patientUuid")
	private String patient;

	@JsonProperty("patientIdentifierTypeUuid")
	private String patientIdentifierType;

	@JsonProperty
	private boolean preferred;
	
	@JsonProperty("locationUuid")
	private String location;

	public SyncPatientIdentifier(HashMap<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPatientIdentifier.class), data, exchange);
	}

	/**
	 * Hopefully computes a unique identifier for the {@link SyncPatientIdentifier}
	 * 
	 * @param data The Camel body.
	 */
	public void computeNewUUID(String patientIdentifierTypeUuid, HashMap<String,String> data) {
		setUuid(UUID.nameUUIDFromBytes((patientIdentifierTypeUuid +
				data.get(Constants.OBJECT_KEY) + this.getIdentifier()).getBytes()).toString());
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getPatientIdentifierType() {
		return patientIdentifierType;
	}

	public void setPatientIdentifierType(String patientIdentifierType) {
		this.patientIdentifierType = patientIdentifierType;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}

package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;

public class SyncPatientIdentifier extends SyncDataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public SyncPatientIdentifier(Map<String, String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPatientIdentifier.class), data, exchange);
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

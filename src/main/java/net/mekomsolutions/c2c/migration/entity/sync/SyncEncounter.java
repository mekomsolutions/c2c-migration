package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;

/**
 * A Person ready to be marshaled as a message
 * compatible with the OpenMRS Camel component.
 * 
 */
public class SyncEncounter extends SyncEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @JsonProperty("encounterTypeUuid")
    private String encounterType;

    @JsonProperty("patientUuid")
    private String patient;

    @JsonProperty("locationUuid")
    private String location;

    @JsonProperty("formUuid")
    private String form;

    @JsonProperty
    private List<Integer> encounterDatetime;

    @JsonProperty("visitUuid")
    private String visit;
	
	public SyncEncounter(String uuid) {
		super(Utils.getModelClassFullFromType(SyncEncounter.class), uuid);
	}
	
	public SyncEncounter(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncEncounter.class), data, exchange);
	}

	public String getEncounterType() {
		return encounterType;
	}

	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public List<Integer> getEncounterDatetime() {
		return encounterDatetime;
	}

	public void setEncounterDatetime(List<Integer> encounterDatetime) {
		this.encounterDatetime = encounterDatetime;
	}

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		this.visit = visit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;

/**
 * A Visit ready to be marshaled as a message
 * compatible with the OpenMRS Camel component.
 * 
 */
public class SyncVisit extends SyncEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("visitTypeUuid")
	private String visitType;

	@JsonProperty("patientUuid")
	private String patient;

	@JsonProperty("locationUuid")
	private String location;
    
	@JsonProperty("dateStarted")
    private List<Integer> dateStarted;

    @JsonProperty("dateStopped")
    private List<Integer> dateStopped;
	
    @JsonProperty("indicationConceptUuid")
    private String indicationConcept;

    public SyncVisit(String uuid) {
		super(Utils.getModelClassFullFromType(SyncVisit.class), uuid);
	}

	public SyncVisit(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncVisit.class), data, exchange);
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
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

	public List<Integer> getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(List<Integer> dateStarted) {
		this.dateStarted = dateStarted;
	}

	public List<Integer> getDateStopped() {
		return dateStopped;
	}

	public void setDateStopped(List<Integer> dateStopped) {
		this.dateStopped = dateStopped;
	}

	public String getIndicationConcept() {
		return indicationConcept;
	}

	public void setIndicationConcept(String indicationConcept) {
		this.indicationConcept = indicationConcept;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

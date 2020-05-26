package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;

/**
 * A Patient ready to be marshaled as a message
 * compatible with the OpenMRS Camel component.
 * 
 */
public class SyncPatient extends SyncDataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String gender;
	
	@JsonProperty
	private List<Integer> birthdate;
	
	@JsonProperty
	private String birthdateEstimated;
	
	@JsonProperty
	private boolean dead;
	
	@JsonProperty
	private List<Integer> deathDate;
	
	@JsonProperty
	private String causeOfDeathUuid;
	
	@JsonProperty
	private String deathdateEstimated;
	
	@JsonProperty
	private String birthtime;
	
	@JsonProperty
	private String allergyStatus;
	
	@JsonProperty
	private String patientCreatorUuid;
	
	@JsonProperty
	private List<Integer> patientDateCreated;
	
	@JsonProperty
	private String patientChangedByUuid;
	
	@JsonProperty
	private List<Integer> patientDateChanged;
	
	@JsonProperty
	private boolean patientVoided;
	
	@JsonProperty
	private String 	patientVoidedByUuid;
	
	@JsonProperty
	private List<Integer> patientDateVoided;
	
	@JsonProperty
	private String patientVoidReason;

	public SyncPatient(String uuid) {
		super(Utils.getModelClassFullFromType(SyncPatient.class), uuid);
	}

	public SyncPatient(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPatient.class), data, exchange);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Integer> getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(List<Integer> birthdate) {
		this.birthdate = birthdate;
	}

	public String getBirthdateEstimated() {
		return birthdateEstimated;
	}

	public void setBirthdateEstimated(String birthdateEstimated) {
		this.birthdateEstimated = birthdateEstimated;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public List<Integer> getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(List<Integer> deathDate) {
		this.deathDate = deathDate;
	}

	public String getCauseOfDeathUuid() {
		return causeOfDeathUuid;
	}

	public void setCauseOfDeathUuid(String causeOfDeathUuid) {
		this.causeOfDeathUuid = causeOfDeathUuid;
	}

	public String getDeathdateEstimated() {
		return deathdateEstimated;
	}

	public void setDeathdateEstimated(String deathdateEstimated) {
		this.deathdateEstimated = deathdateEstimated;
	}

	public String getBirthtime() {
		return birthtime;
	}

	public void setBirthtime(String birthtime) {
		this.birthtime = birthtime;
	}

	public String getAllergyStatus() {
		return allergyStatus;
	}

	public void setAllergyStatus(String allergyStatus) {
		this.allergyStatus = allergyStatus;
	}

	public String getPatientCreatorUuid() {
		return patientCreatorUuid;
	}

	public void setPatientCreatorUuid(String patientCreatorUuid) {
		this.patientCreatorUuid = patientCreatorUuid;
	}

	public List<Integer> getPatientDateCreated() {
		return patientDateCreated;
	}

	public void setPatientDateCreated(List<Integer> patientDateCreated) {
		this.patientDateCreated = patientDateCreated;
	}

	public String getPatientChangedByUuid() {
		return patientChangedByUuid;
	}

	public void setPatientChangedByUuid(String patientChangedByUuid) {
		this.patientChangedByUuid = patientChangedByUuid;
	}

	public List<Integer> getPatientDateChanged() {
		return patientDateChanged;
	}

	public void setPatientDateChanged(List<Integer> patientDateChanged) {
		this.patientDateChanged = patientDateChanged;
	}

	public boolean isPatientVoided() {
		return patientVoided;
	}

	public void setPatientVoided(boolean patientVoided) {
		this.patientVoided = patientVoided;
	}

	public String getPatientVoidedByUuid() {
		return patientVoidedByUuid;
	}

	public void setPatientVoidedByUuid(String patientVoidedByUuid) {
		this.patientVoidedByUuid = patientVoidedByUuid;
	}

	public List<Integer> getPatientDateVoided() {
		return patientDateVoided;
	}

	public void setPatientDateVoided(List<Integer> patientDateVoided) {
		this.patientDateVoided = patientDateVoided;
	}

	public String getPatientVoidReason() {
		return patientVoidReason;
	}

	public void setPatientVoidReason(String patientVoidReason) {
		this.patientVoidReason = patientVoidReason;
	}

}

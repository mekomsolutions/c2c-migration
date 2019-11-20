package net.mekomsolutions.c2c.extract.Entity.OpenMRSEntity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Utils;

public class OpenMRSPersonName extends OpenMRSEntity {

	@JsonProperty
	private String creatorUuid;
	
	@JsonProperty
	private List<Integer> dateCreated;
	
	@JsonProperty
	private List<Integer> dateChanged;
	
	@JsonProperty
	private String changedByUuid;
	
	@JsonProperty
	private boolean voided;
	
	@JsonProperty
	private String voidedByUuid;
	
	@JsonProperty
	private List<Integer> dateVoided;
	
	@JsonProperty
	private String voidReason;
	
	@JsonProperty
	private boolean retired;
	
	@JsonProperty
	private String retiredByUuid;
	
	@JsonProperty
	private List<Integer> dateRetired;
	
	@JsonProperty
	private String retiredReason;
	
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

	public OpenMRSPersonName(String uuid) {
		super(Utils.getModelClassNameFromType(OpenMRSPersonName.class), uuid);
	}

	public String getCreatorUuid() {
		return creatorUuid;
	}

	public void setCreatorUuid(String creatorUuid) {
		this.creatorUuid = creatorUuid;
	}

	public List<Integer> getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(List<Integer> dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<Integer> getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(List<Integer> dateChanged) {
		this.dateChanged = dateChanged;
	}

	public String getChangedByUuid() {
		return changedByUuid;
	}

	public void setChangedByUuid(String changedByUuid) {
		this.changedByUuid = changedByUuid;
	}

	public boolean isVoided() {
		return voided;
	}

	public void setVoided(boolean voided) {
		this.voided = voided;
	}

	public String getVoidedByUuid() {
		return voidedByUuid;
	}

	public void setVoidedByUuid(String voidedByUuid) {
		this.voidedByUuid = voidedByUuid;
	}

	public List<Integer> getDateVoided() {
		return dateVoided;
	}

	public void setDateVoided(List<Integer> dateVoided) {
		this.dateVoided = dateVoided;
	}

	public String getVoidReason() {
		return voidReason;
	}

	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}

	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	public String getRetiredByUuid() {
		return retiredByUuid;
	}

	public void setRetiredByUuid(String retiredByUuid) {
		this.retiredByUuid = retiredByUuid;
	}

	public List<Integer> getDateRetired() {
		return dateRetired;
	}

	public void setDateRetired(List<Integer> dateRetired) {
		this.dateRetired = dateRetired;
	}

	public String getRetiredReason() {
		return retiredReason;
	}

	public void setRetiredReason(String retiredReason) {
		this.retiredReason = retiredReason;
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

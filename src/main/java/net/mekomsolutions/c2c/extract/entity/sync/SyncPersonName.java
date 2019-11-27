package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Utils;

/**
 * A PersonName ready to be marshaled in a message,
 * compatible with the OpenMRS Camel component.
 * 
 */
public class SyncPersonName extends SyncEntity {

	@JsonProperty
	private String creatorUuid;

	@JsonProperty
	private List<Integer> dateCreated;

	@JsonProperty
	private String changedByUuid;

	@JsonProperty
	private List<Integer> dateChanged;

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
	private boolean preferred;

	@JsonProperty
	private String personUuid;

	@JsonProperty
	private String prefix;

	@JsonProperty
	private String givenName;

	@JsonProperty
	private String middleName;

	@JsonProperty
	private String familyNamePrefix;

	@JsonProperty
	private String familyName;

	@JsonProperty
	private String familyName2;

	@JsonProperty
	private String familyNameSuffix2;

	@JsonProperty
	private String degree;

	public SyncPersonName(String uuid) {
		super(Utils.getModelClassFullFromType(SyncPersonName.class), uuid);
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

	public String getChangedByUuid() {
		return changedByUuid;
	}

	public void setChangedByUuid(String changedByUuid) {
		this.changedByUuid = changedByUuid;
	}

	public List<Integer> getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(List<Integer> dateChanged) {
		this.dateChanged = dateChanged;
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

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public String getPersonUuid() {
		return personUuid;
	}

	public void setPersonUuid(String personUuid) {
		this.personUuid = personUuid;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFamilyNamePrefix() {
		return familyNamePrefix;
	}

	public void setFamilyNamePrefix(String familyNamePrefix) {
		this.familyNamePrefix = familyNamePrefix;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyName2() {
		return familyName2;
	}

	public void setFamilyName2(String familyName2) {
		this.familyName2 = familyName2;
	}

	public String getFamilyNameSuffix2() {
		return familyNameSuffix2;
	}

	public void setFamilyNameSuffix2(String familyNameSuffix2) {
		this.familyNameSuffix2 = familyNameSuffix2;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

}

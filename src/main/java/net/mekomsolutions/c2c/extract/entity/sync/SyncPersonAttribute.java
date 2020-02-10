package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.UUID;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;

public class SyncPersonAttribute extends SyncEntity {

	@JsonProperty("changedByUuid")
	private String changedBy;

	@JsonProperty
	private String dateChanged;

	@JsonProperty
	private boolean voided;

	@JsonProperty("voidedByUuid")
	private String voidedBy;

	@JsonProperty
	private String dateVoided;

	@JsonProperty
	private String voidReason;

	@JsonProperty
	private boolean retired;

	@JsonProperty("retiredByUuid")
	private String retiredBy;

	@JsonProperty
	private String dateRetired;

	@JsonProperty
	private String retiredReason;

	@JsonProperty
	private String value;

	@JsonProperty("personUuid")
	private String person;

	@JsonProperty("personAttributeTypeUuid")
	private String personAttributeType;

	public SyncPersonAttribute(HashMap<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPersonAttribute.class), data, exchange);
	}

	/**
	 * Hopefully computes a unique identifier for the {@link SyncPersonAttribute}
	 * 
	 * @param data The Camel body.
	 */
	public void computeNewUUID(String personAttributeTypeUuid, HashMap<String,String> data) {
		setUuid(UUID.nameUUIDFromBytes((personAttributeTypeUuid +
				data.get(Constants.OBJECT_KEY) + this.getValue()).getBytes()).toString());
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public String getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(String dateChanged) {
		this.dateChanged = dateChanged;
	}

	public boolean isVoided() {
		return voided;
	}

	public void setVoided(boolean voided) {
		this.voided = voided;
	}

	public String getVoidedBy() {
		return voidedBy;
	}

	public void setVoidedBy(String voidedBy) {
		this.voidedBy = voidedBy;
	}

	public String getDateVoided() {
		return dateVoided;
	}

	public void setDateVoided(String dateVoided) {
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

	public String getRetiredBy() {
		return retiredBy;
	}

	public void setRetiredBy(String retiredBy) {
		this.retiredBy = retiredBy;
	}

	public String getDateRetired() {
		return dateRetired;
	}

	public void setDateRetired(String dateRetired) {
		this.dateRetired = dateRetired;
	}

	public String getRetiredReason() {
		return retiredReason;
	}

	public void setRetiredReason(String retiredReason) {
		this.retiredReason = retiredReason;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getPersonAttributeType() {
		return personAttributeType;
	}

	public void setPersonAttributeType(String personAttributeType) {
		this.personAttributeType = personAttributeType;
	}

}

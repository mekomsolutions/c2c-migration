package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Utils;

public class SyncPersonAttribute extends SyncEntity {

	@JsonProperty
	private String changedByUuid;

	@JsonProperty
	private String dateChanged;

	@JsonProperty
	private boolean voided;

	@JsonProperty
	private String voidedByUuid;

	@JsonProperty
	private String dateVoided;

	@JsonProperty
	private String voidReason;

	@JsonProperty
	private boolean retired;

	@JsonProperty
	private String retiredByUuid;

	@JsonProperty
	private String dateRetired;

	@JsonProperty
	private String retiredReason;

	@JsonProperty
	private String value;

	@JsonProperty
	private String personUuid;

	@JsonProperty
	private String personAttributeTypeUuid;

	public SyncPersonAttribute(HashMap<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPersonAttribute.class), data, exchange);
	}

	public String getChangedByUuid() {
		return changedByUuid;
	}

	public void setChangedByUuid(String changedByUuid) {
		this.changedByUuid = changedByUuid;
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

	public String getVoidedByUuid() {
		return voidedByUuid;
	}

	public void setVoidedByUuid(String voidedByUuid) {
		this.voidedByUuid = voidedByUuid;
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

	public String getRetiredByUuid() {
		return retiredByUuid;
	}

	public void setRetiredByUuid(String retiredByUuid) {
		this.retiredByUuid = retiredByUuid;
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

	public String getPersonUuid() {
		return personUuid;
	}

	public void setPersonUuid(String personUuid) {
		this.personUuid = personUuid;
	}

	public String getPersonAttributeTypeUuid() {
		return personAttributeTypeUuid;
	}

	public void setPersonAttributeTypeUuid(String personAttributeTypeUuid) {
		this.personAttributeTypeUuid = personAttributeTypeUuid;
	}

}

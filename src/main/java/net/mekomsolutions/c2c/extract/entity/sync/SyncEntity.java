package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Utils;

/**
 * Base class that documents the existence of the 
 * mandatory fields of an entity. Ie, 'uuid', modelClass'...
 * 
 */
public class SyncEntity {

	@JsonIgnore
	private String modelClassName;

	@JsonProperty
	private String uuid;

	@JsonProperty
	private String creatorUuid;

	@JsonProperty
	private List<Integer> dateCreated;
	
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
	
	public SyncEntity(String modelClassName, String uuid) {
		super();
		this.uuid = uuid;
		this.modelClassName = modelClassName;
	}
	
	/**
	 * 
	 * A convenient constructor for the SyncEntity objects that sets default values to 
	 * the {@link SyncEntity#modelClassName modelClassName}, {@link SyncEntity#uuid uuid},
	 * {@link SyncEntity#creatorUuid creatorUuid} and {@link SyncEntity#dateCreated dateCreated} fields,
	 * guessed from the passed parameters.
	 * 
	 * @param modelClassName
	 * @param data
	 * @param exchange
	 * @throws Exception 
	 */
	public SyncEntity(String modelClassName, HashMap<String,String> data, Exchange exchange) throws Exception {
		super();
		this.modelClassName = modelClassName;
		
		String userUuid = exchange.getContext().resolvePropertyPlaceholders("{{user.uuid}}");
		String defaultUserLight = Utils.getModelClassLight("User",
				UUID.fromString(userUuid));
		this.uuid = UUID.nameUUIDFromBytes(data.get("objKey").getBytes()).toString();
		this.creatorUuid = defaultUserLight;
		TypeConverter converter = exchange.getContext().getTypeConverter();
		this.dateCreated = Utils.dateLongToArray(converter.convertTo(Long.class, data.get("lastModified")));
	}

	public String getModelClassName() {
		return modelClassName;
	}

	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

}

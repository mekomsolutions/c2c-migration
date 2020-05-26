package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Documents the mandatory fields for Data type of entities
  * 
 */
public class SyncDataEntity extends SyncEntity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private boolean voided;

	@JsonProperty("voidedByUuid")
	private String voidedBy;

	@JsonProperty
	private String dateVoided;

	@JsonProperty
	private String voidReason;

	public SyncDataEntity(String modelClassName, String uuid) {
		super(uuid, modelClassName);
	}

	/**
	 * 
	 * A convenient constructor for the SyncEntity objects that sets default values to 
	 * the {@link SyncDataEntity#modelClassName modelClassName}, {@link SyncDataEntity#uuid uuid},
	 * {@link SyncDataEntity#creatorUuid creatorUuid} and {@link SyncDataEntity#dateCreated dateCreated} fields,
	 * guessed from the passed parameters.
	 * 
	 * @param modelClassName
	 * @param data
	 * @param exchange
	 * @throws Exception 
	 */
	public SyncDataEntity(String modelClassName, Map<String, String> data, Exchange exchange) throws Exception {
		super(modelClassName, data, exchange);
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

}

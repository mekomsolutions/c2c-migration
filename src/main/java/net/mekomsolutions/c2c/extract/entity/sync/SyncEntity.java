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

	public String getUuid() {
		return uuid;
	}

	public String getModelClassName() {
		return modelClassName;
	}

	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
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
	
}

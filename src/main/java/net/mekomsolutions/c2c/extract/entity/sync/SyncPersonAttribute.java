package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;

public class SyncPersonAttribute extends SyncEntity {

	@JsonProperty
	private String value;

	@JsonProperty("personUuid")
	private String person;

	@JsonProperty("personAttributeTypeUuid")
	private String personAttributeType;

	public SyncPersonAttribute(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPersonAttribute.class), data, exchange);
	}

	/**
	 * Hopefully computes a unique identifier for the {@link SyncPersonAttribute}
	 * 
	 * @param data The Camel body.
	 */
	public void computeNewUUID(String personAttributeTypeUuid, Map<String, String> data) {
		setUuid(UUID.nameUUIDFromBytes((personAttributeTypeUuid +
				data.get(Constants.OBJECT_KEY) + this.getValue()).getBytes()).toString());
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

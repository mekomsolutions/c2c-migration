package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;

public class SyncPersonAttribute extends SyncDataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty
	private String value;

	@JsonProperty("personUuid")
	private String person;

	@JsonProperty("personAttributeTypeUuid")
	private String personAttributeType;

	public SyncPersonAttribute(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPersonAttribute.class), data, exchange);
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

package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.Address;

public class SyncPersonAddress extends SyncDataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("address")
	private Address address;

	@JsonProperty("personUuid")
	private String person;

	@JsonProperty
	private boolean preferred;

	public SyncPersonAddress(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPersonAddress.class), data, exchange);
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

}

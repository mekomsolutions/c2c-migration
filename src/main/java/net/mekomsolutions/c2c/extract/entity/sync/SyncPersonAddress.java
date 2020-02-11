package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.openmrs.sync.component.common.Address;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Constants;
import net.mekomsolutions.c2c.extract.Utils;

public class SyncPersonAddress extends SyncEntity {

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

package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.extract.Utils;

/**
 * A PersonName ready to be marshaled in a message,
 * compatible with the OpenMRS Camel component.
 * 
 */
public class SyncPersonName extends SyncEntity {

	@JsonProperty
	private boolean preferred;

	@JsonProperty("personUuid")
	private String person;

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

	public SyncPersonName(HashMap<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncPersonName.class), data, exchange);
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public String getPerson() {
		return person;
	}
	
	public void setPerson(String person) {
		this.person = person;
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

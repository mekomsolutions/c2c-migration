package net.mekomsolutions.c2c.extract.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person extends BaseEntity {
	
	@JsonProperty("patientUuid")
	private String patient;

	@JsonProperty
	private String givenName;
	
	@JsonProperty
	private String middleName;
	
	@JsonProperty
	private String familyName;

	public Person(String modelClassName, String uuid, String person, String givenName, String middleName, String familyName) {
		super(modelClassName ,uuid);
		this.patient = person;
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
	}

}

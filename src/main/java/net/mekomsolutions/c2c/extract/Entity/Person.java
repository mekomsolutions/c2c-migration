package net.mekomsolutions.c2c.extract.Entity;

public class Person {

	
	public Person(String person, String givenName, String middleName, String familyName) {
		super();
		this.person = person;
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
	}

	private String person;

	private String givenName;
	
	private String middleName;
	
	private String familyName;

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
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

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	
}

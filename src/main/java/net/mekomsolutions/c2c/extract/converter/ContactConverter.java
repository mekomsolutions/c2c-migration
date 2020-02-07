package net.mekomsolutions.c2c.extract.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.springframework.util.StringUtils;

import net.mekomsolutions.c2c.extract.Utils;
import net.mekomsolutions.c2c.extract.entity.Contact;
import net.mekomsolutions.c2c.extract.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.extract.entity.sync.SyncPersonAttribute;

@Converter
public class ContactConverter {

	private static String OBJECT_KEY = "objKey";
	private static String PATIENT_KEY = "patientKey";

	private static String FIRST_NAME = "firstname";
	private static String LAST_NAME = "lastname";
	private static String MIDDLE_NAME = "middlename";

	private static String CELL_PHONE = "cellphone";
	private static String HOME_PHONE = "homephone";
	private static String WORK_PHONE = "workphone";

	private static String CONTACT_TYPE = "type";

	private static String RELATIONSHIP = "relationship";

	private static String ADDRESS_LINE_1 = "addressline1";
	private static String ADDRESS_LINE_2 = "addressline2";

	@Converter
	public Contact toContact(HashMap<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<SyncEntity>();

		// Contact Person Name:
		SyncPersonAttribute contactPersonName = new SyncPersonAttribute(data, exchange);
		contactPersonName.setValue(Utils.concatName(new LinkedList<String>(
				Arrays.asList(data.get(FIRST_NAME), data.get(MIDDLE_NAME), data.get(LAST_NAME)))));

		String personNameAttributeTypeUuid = exchange.getContext().
				resolvePropertyPlaceholders("{{pat.contactPersonName.uuid}}");
		contactPersonName.setPersonAttributeTypeUuid(
				Utils.getModelClassLight("PersonAttributeType",
						UUID.fromString(personNameAttributeTypeUuid)));

		contactPersonName.setPersonUuid(
				Utils.getModelClassLight("Patient",
						UUID.nameUUIDFromBytes(data.get(PATIENT_KEY).getBytes())));

		// Override the default UUID.
		// UUID can not be set from the objectKey only, as it would be the same for other personAttributes below (Phone Number, Address...)
		// Setting it as a concatenation of other values, hopefully creating a unique key.
		contactPersonName.setUuid(
				UUID.nameUUIDFromBytes((contactPersonName.getPersonAttributeTypeUuid() +
						data.get(OBJECT_KEY) +
						contactPersonName.getValue()).getBytes()).toString());

		allEntities.add(contactPersonName);

		// Contact Person Phone Number
		String contactPersonPhoneNmubers = Utils.concatPhoneNumber(new LinkedList<String>(
				Arrays.asList(data.get(CELL_PHONE), data.get(HOME_PHONE), data.get(WORK_PHONE))));

		if (! StringUtils.isEmpty(contactPersonPhoneNmubers)) {
			SyncPersonAttribute personPhoneNumber = new SyncPersonAttribute(data, exchange);
			personPhoneNumber.setValue(Utils.concatPhoneNumber(new LinkedList<String>(
					Arrays.asList(data.get(CELL_PHONE), data.get(HOME_PHONE), data.get(WORK_PHONE)))));

			String phoneNumberAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{pat.contactPersonPhone.uuid}}");
			personPhoneNumber.setPersonAttributeTypeUuid(
					Utils.getModelClassLight("PersonAttributeType",
							UUID.fromString(phoneNumberAttributeTypeUuid)));

			personPhoneNumber.setPersonUuid(
					Utils.getModelClassLight("Patient",
							UUID.nameUUIDFromBytes(data.get(PATIENT_KEY).getBytes())));

			personPhoneNumber.setUuid(
					UUID.nameUUIDFromBytes((contactPersonName.getPersonAttributeTypeUuid() + 
							data.get(OBJECT_KEY) + 
							personPhoneNumber.getValue()).getBytes()).toString());

			allEntities.add(personPhoneNumber);
		}


		// Contact Type
		if (! StringUtils.isEmpty(data.get(CONTACT_TYPE))) {
			SyncPersonAttribute contactPersonContactType = new SyncPersonAttribute(data, exchange);
			contactPersonContactType.setValue(data.get(CONTACT_TYPE));

			String contactTypeAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{pat.contactContactType.uuid}}");
			contactPersonContactType.setPersonAttributeTypeUuid(
					Utils.getModelClassLight("PersonAttributeType",
							UUID.fromString(contactTypeAttributeTypeUuid)));

			contactPersonContactType.setPersonUuid(
					Utils.getModelClassLight("Patient",
							UUID.nameUUIDFromBytes(data.get(PATIENT_KEY).getBytes())));

			contactPersonContactType.setUuid(
					UUID.nameUUIDFromBytes((data.get(OBJECT_KEY) + 
							contactPersonContactType.getValue()).getBytes()).toString());

			allEntities.add(contactPersonContactType);
		}

		// Contact Person Relationship
		if (! StringUtils.isEmpty(data.get(RELATIONSHIP))) {
			SyncPersonAttribute contactPersonRelationship = new SyncPersonAttribute(data, exchange);
			contactPersonRelationship.setValue(data.get(RELATIONSHIP));

			String contactTypeAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{pat.contactRelationship.uuid}}");
			contactPersonRelationship.setPersonAttributeTypeUuid(
					Utils.getModelClassLight("PersonAttributeType",
							UUID.fromString(contactTypeAttributeTypeUuid)));

			contactPersonRelationship.setPersonUuid(
					Utils.getModelClassLight("Patient",
							UUID.nameUUIDFromBytes(data.get(PATIENT_KEY).getBytes())));

			contactPersonRelationship.setUuid(
					UUID.nameUUIDFromBytes((data.get(OBJECT_KEY) + 
							contactPersonRelationship.getValue()).getBytes()).toString());

			allEntities.add(contactPersonRelationship);
		}

		// Contact Person Address
		String contactPersonAddresses = Utils.concatAddresses(new LinkedList<String>(
				Arrays.asList(data.get(ADDRESS_LINE_1), data.get(ADDRESS_LINE_2))));

		if (! StringUtils.isEmpty(contactPersonAddresses)) {
			SyncPersonAttribute contactPersonAddress = new SyncPersonAttribute(data, exchange);
			contactPersonAddress.setValue(contactPersonAddresses);

			String contactPersonAddressAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{pat.contactRelationship.uuid}}");
			contactPersonAddress.setPersonAttributeTypeUuid(
					Utils.getModelClassLight("PersonAttributeType",
							UUID.fromString(contactPersonAddressAttributeTypeUuid)));

			contactPersonAddress.setPersonUuid(
					Utils.getModelClassLight("Patient",
							UUID.nameUUIDFromBytes(data.get(PATIENT_KEY).getBytes())));

			contactPersonAddress.setUuid(
					UUID.nameUUIDFromBytes((data.get(OBJECT_KEY) + 
							contactPersonAddress.getValue()).getBytes()).toString());

			allEntities.add(contactPersonAddress);
		}

		return new Contact(allEntities);
	}
}

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

	@Converter
	public Contact toContact(HashMap<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<SyncEntity>();

		// Contact Person Name:
		SyncPersonAttribute contactPersonName = new SyncPersonAttribute(data, exchange);
		contactPersonName.setValue(Utils.concatName(new LinkedList<String>(
				Arrays.asList(data.get("firstname"), data.get("middlename"), data.get("lastname")))));
		
		String personNameAttributeTypeUuid = exchange.getContext().
				resolvePropertyPlaceholders("{{pat.contactPersonName.uuid}}");
		contactPersonName.setPersonAttributeTypeUuid(
				Utils.getModelClassLight("PersonAttributeType",
						UUID.fromString(personNameAttributeTypeUuid)));
		
		contactPersonName.setPersonUuid(
				Utils.getModelClassLight("Patient",
						UUID.nameUUIDFromBytes(data.get("patientKey").getBytes())));

		// Override the default UUID.
		// UUID can not be set from the objectKey only, as it would be the same for other personAttributes below (Phone Number, Address...)
		// Setting it as a concatenation of other values, hopefully creating a unique key.
		contactPersonName.setUuid(
				UUID.nameUUIDFromBytes((contactPersonName.getPersonAttributeTypeUuid() +
						data.get("objectKey") +
						contactPersonName.getValue()).getBytes()).toString());
		
		allEntities.add(contactPersonName);
		
		// Contact Person Phone Number
		String phoneNmubers = Utils.concatPhoneNumber(new LinkedList<String>(
				Arrays.asList(data.get("cellphone"), data.get("homephone"), data.get("workphone"))));
		
		if (! StringUtils.isEmpty(phoneNmubers)) {
			SyncPersonAttribute contactPersonPhoneNumber = new SyncPersonAttribute(data, exchange);
			contactPersonPhoneNumber.setValue(Utils.concatPhoneNumber(new LinkedList<String>(
					Arrays.asList(data.get("cellphone"), data.get("homephone"), data.get("workphone")))));
			
			String contactPhoneNumberAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{pat.contactPersonPhone.uuid}}");
			contactPersonPhoneNumber.setPersonAttributeTypeUuid(
					Utils.getModelClassLight("PersonAttributeType",
					UUID.fromString(contactPhoneNumberAttributeTypeUuid)));
			
			contactPersonPhoneNumber.setPersonUuid(
					Utils.getModelClassLight("Patient",
							UUID.nameUUIDFromBytes(data.get("patientKey").getBytes())));
			
			contactPersonPhoneNumber.setUuid(
					UUID.nameUUIDFromBytes((contactPersonName.getPersonAttributeTypeUuid() + 
							data.get("objectKey") + 
							contactPersonPhoneNumber.getValue()).getBytes()).toString());
			
			allEntities.add(contactPersonPhoneNumber);
		}
		
		// Contact Person Address
		

		// Contact Person Relationship
		//			b47a9c55-4d28-4549-a223-b16d54b8fbbf

		return new Contact(allEntities);
	}
}

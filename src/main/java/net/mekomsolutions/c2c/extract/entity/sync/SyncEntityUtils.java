package net.mekomsolutions.c2c.extract.entity.sync;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.extract.Utils;

public class SyncEntityUtils {
	
	public static void createAndAddPersonAttribute(String personAttributeTypeProperty, String value, String baseKey, HashMap<String,String> data, Exchange exchange, List<SyncEntity> allEntities) throws Exception {
		if (Utils.hasKeyOrValue(value)) {
			SyncPersonAttribute pa = new SyncPersonAttribute(data, exchange);
			pa.setValue(value);
			String personAddressAttributeTypeUuid = exchange.getContext().
					resolvePropertyPlaceholders("{{" + personAttributeTypeProperty + "}}");
			pa.setPersonAttributeType(Utils.getModelClassLight("PersonAttributeType",
					UUID.fromString(personAddressAttributeTypeUuid)));
			pa.setPerson(Utils.getModelClassLight("Patient",
					UUID.nameUUIDFromBytes(data.get(baseKey).getBytes())));
			// Do not forget to override the UUID to a unique one.
			pa.computeNewUUID(personAddressAttributeTypeUuid, data);

			allEntities.add(pa);
		}
	}
}

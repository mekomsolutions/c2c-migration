package net.mekomsolutions.c2c.migration.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.LabTest;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;

@Converter
public class LabTestConverter {

	private static final String DATE = "date";
	private static final String DESCRIPTION = "description";

	/**
	 * Transform the data input, passed as a parameter, into the appropriate sub entities.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.migration.entity.LabTest
	 */
	@Converter
	public LabTest toLabTest(Map<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<>();

		String patientLight = Utils.getModelClassLight("Patient", 
				UUID.nameUUIDFromBytes(data.get(Constants.PATIENT_KEY).getBytes()));

		// TODO: Order

		return new LabTest(allEntities);
	}

}

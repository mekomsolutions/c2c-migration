package net.mekomsolutions.c2c.migration.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.LabTest;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;

@Converter
public class LabTestConverter {

	private static final String NAME = "name";
	private static final String DISPENSED = "dispensed";
	private static final String PRESCRIBED = "prescribed";

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

		// Since the Encounter UUID is generated based on the Visit 'objKey' and the EncounterType UUID,
		// we can compute the correct Encounter UUID from the 'visitKey' of the Diagnosis object.
		String encounterTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{encounterType.consultation.uuid}}");
		String encounterLight = Utils.getModelClassLight("Encounter",
				UUID.fromString(SyncEntityUtils.computeNewUUID(encounterTypeUuid, data.get("visitKey"))));

		// The source order does not look like the Order model as it is in OpenMRS.
		// Recording the order as an obs group
		SyncObservation obsGroup = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "labtest", "");
		String obsGroupUuidLight = Utils.getModelClassLight("Observation", UUID.fromString(obsGroup.getUuid()));

		// Lab Test Name
		SyncObservation name = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "labtestName", "");
		name.setValueText(data.get(NAME));
		name.setObsGroup(obsGroupUuidLight);

		TypeConverter converter = exchange.getContext().getTypeConverter();

		// Lab Test Dispensed
		SyncObservation dispensed = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "labtestDispensed", "");
		dispensed.setValueNumeric(converter.convertTo(String.class, data.get(DISPENSED)));
		dispensed.setObsGroup(obsGroupUuidLight);

		// Lab Test Prescribed
		SyncObservation prescribed = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "labtestPrescribed", "");
		prescribed.setValueNumeric(converter.convertTo(String.class, data.get(PRESCRIBED)));
		prescribed.setObsGroup(obsGroupUuidLight);

		allEntities.add(obsGroup);
		allEntities.add(name);
		allEntities.add(dispensed);
		allEntities.add(prescribed);
		
		return new LabTest(allEntities);
	}

}

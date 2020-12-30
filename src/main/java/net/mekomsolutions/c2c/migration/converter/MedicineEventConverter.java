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
import net.mekomsolutions.c2c.migration.entity.MedicineEvent;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;

@Converter
public class MedicineEventConverter {

	private static final String NAME = "name";
	private static final String DISPENSED = "dispensed";
	private static final String PRESCRIBED = "prescribed";
	private static final String RECOMMENDED_DOSE = "recommendeddose";

	/**
	 * Transform the data input, passed as a parameter, into the appropriate sub entities.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.migration.entity.MedicineEvent
	 */
	@Converter
	public MedicineEvent toMedicineEvent(Map<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<>();

		String patientLight = Utils.getModelClassLight("Patient", 
				UUID.nameUUIDFromBytes(data.get(Constants.PATIENT_KEY).getBytes()));

		// Since the Encounter UUID is generated based on the Visit 'objKey' and the EncounterType UUID,
		// we can compute the correct Encounter UUID from the 'visitKey' of the Diagnosis object.
		String encounterTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{encounterType.consultation.uuid}}");
		String encounterLight = Utils.getModelClassLight("Encounter",
				UUID.fromString(SyncEntityUtils.computeNewUUID(encounterTypeUuid, data.get(Constants.VISIT_KEY))));

		// The source order does not look like the Order model as it is in OpenMRS.
		// Recording the order as an obs group
		SyncObservation obsGroup = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "drugorder", "");
		String obsGroupUuidLight = Utils.getModelClassLight("Observation", UUID.fromString(obsGroup.getUuid()));
		
		SyncObservation name = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "drugorderName", "");
		name.setValueText(data.get(NAME));
		name.setObsGroup(obsGroupUuidLight);
		
		TypeConverter converter = exchange.getContext().getTypeConverter();

		SyncObservation dispensed = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "drugorderDispensed", "");
		dispensed.setValueNumeric(converter.convertTo(String.class, data.get(DISPENSED)));
		dispensed.setObsGroup(obsGroupUuidLight);
		
		SyncObservation prescribed = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "drugorderPrescribed", "");
		prescribed.setValueNumeric(converter.convertTo(String.class, data.get(PRESCRIBED)));
		prescribed.setObsGroup(obsGroupUuidLight);
		
		allEntities.add(obsGroup);
		allEntities.add(name);
		allEntities.add(dispensed);
		allEntities.add(prescribed);
		
		if (data.get(RECOMMENDED_DOSE) != null) {
			SyncObservation recommendedDose = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "drugorderRecommendedDose", "");
			recommendedDose.setValueNumeric(converter.convertTo(String.class, data.get(RECOMMENDED_DOSE)));
			recommendedDose.setObsGroup(obsGroupUuidLight);
			allEntities.add(recommendedDose);
		}
		
		return new MedicineEvent(allEntities);
	}

}

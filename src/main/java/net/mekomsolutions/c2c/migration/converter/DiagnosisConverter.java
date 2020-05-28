package net.mekomsolutions.c2c.migration.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.Diagnosis;
import net.mekomsolutions.c2c.migration.entity.Visit;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEncounter;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;
import net.mekomsolutions.c2c.migration.entity.sync.SyncVisit;

@Converter
public class DiagnosisConverter {

	private static final String DATE = "date";
	private static final String DESCRIPTION = "description";

	/**
	 * Transform the data input, passed as a parameter, into the appropriate sub entities.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.migration.entity.Diagnosis
	 */
	@Converter
	public Diagnosis toDiagnosis(Map<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<>();

		String patientLight = Utils.getModelClassLight("Patient", 
				UUID.nameUUIDFromBytes(data.get(Constants.PATIENT_KEY).getBytes()));

		// Since the Encounter UUID is generated based on the Visit 'objKey' and the EncounterType UUID,
		// we can compute the correct Encounter UUID from the 'visitKey' of the Diagnosis object.
		String encounterTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{encounterType.consultation.uuid}}");
		String encounterLight = Utils.getModelClassLight("Encounter",
				UUID.fromString(SyncEntityUtils.computeNewUUID(encounterTypeUuid, data.get("visitKey"))));

		if (data.get("type").equals("Diagnosis")) {

			// VisitDiagnoses: The parent obsGroup
			SyncObservation obsGroup = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "visitDiagnoses", "");
			String obsGroupUuidLight = Utils.getModelClassLight("Observation", UUID.fromString(obsGroup.getUuid()));

			// Non-Coded Diagnosis
			SyncObservation nonCodedDiag = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "nonCodedDiagnosis", data.get(DESCRIPTION));
			nonCodedDiag.setValueText(data.get(DESCRIPTION));
			nonCodedDiag.setObsGroup(obsGroupUuidLight);

			// Diagnosis Certainty
			SyncObservation diagCertainty = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "diagnosisCertainty", "");
			String diagConfirmedUuid = exchange.getContext().resolvePropertyPlaceholders("{{concept.diagnosisCertainty.confirmed.uuid}}");
			diagCertainty.setValueCoded(Utils.getModelClassLight("Concept", UUID.fromString(diagConfirmedUuid)));
			diagCertainty.setObsGroup(obsGroupUuidLight);

			// Diagnosis Order
			SyncObservation diagOrder = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "diagnosisOrder", "");
			String diagPrimaryUuid = exchange.getContext().resolvePropertyPlaceholders("{{concept.diagnosisOrder.primary.uuid}}");
			diagOrder.setValueCoded(Utils.getModelClassLight("Concept", UUID.fromString(diagPrimaryUuid)));
			diagOrder.setObsGroup(obsGroupUuidLight);

			allEntities.add(obsGroup);
			allEntities.add(nonCodedDiag);
			allEntities.add(diagCertainty);
			allEntities.add(diagOrder);

		} else if (data.get("type").equals("Complaint")) {

			// Obs: Chief Complaint
			{
				SyncObservation chiefComplaint = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "chiefcomplaint", data.get(DESCRIPTION));
				chiefComplaint.setValueText(data.get(DESCRIPTION));
				allEntities.add(chiefComplaint);
			}			
		}

		return new Diagnosis(allEntities);
	}

}

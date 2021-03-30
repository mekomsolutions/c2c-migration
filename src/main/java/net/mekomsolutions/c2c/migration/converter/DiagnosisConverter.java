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
				UUID.fromString(SyncEntityUtils.computeNewUUID(encounterTypeUuid, data.get(Constants.VISIT_KEY))));

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

			// Bahmni Diagnosis Revised
			SyncObservation diagRevised = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "diagnosisRevised", "");
			diagRevised.setValueCoded(Utils.getModelClassLight("Concept", exchange.getContext().resolvePropertyPlaceholders("{{concept.no.uuid}}")));
			diagRevised.setObsGroup(obsGroupUuidLight);

			// Bahmni Initial Diagnosis
			// Bahmni Initial Diagnosis is saved as a reference to the "VisitDiagnosis" obs UUID (!?)
			SyncObservation initDiag = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "initialDiagnosis", "");
			initDiag.setValueText(obsGroup.getUuid());
			initDiag.setObsGroup(obsGroupUuidLight);

			allEntities.add(obsGroup);
			allEntities.add(nonCodedDiag);
			allEntities.add(diagCertainty);
			allEntities.add(diagOrder);
			allEntities.add(diagRevised);
			allEntities.add(initDiag);

		} else if (data.get("type").equals("Complaint")) {

			// Chief Complaint Data: The parent obsGroup
			SyncObservation obsGroup = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "chiefcomplaintData", "");
			String obsGroupUuidLight = Utils.getModelClassLight("Observation", UUID.fromString(obsGroup.getUuid()));

			// Chief Complaint Coded: Set the value to 'Other'
			SyncObservation chiefComplaintCoded = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "chiefcomplaintCoded", "");
			String otherUuid = exchange.getContext().resolvePropertyPlaceholders("{{concept.chiefcomplaintCoded.other.uuid}}");
			chiefComplaintCoded.setValueCoded(Utils.getModelClassLight("Concept", UUID.fromString(otherUuid)));
			chiefComplaintCoded.setObsGroup(obsGroupUuidLight);

			// Chief Complaint Details: Set the text value
			SyncObservation freeTextComplaint = SyncEntityUtils.createObs(data, exchange, encounterLight, patientLight, "chiefcomplaintDetails", "");
			freeTextComplaint.setValueText(data.get(DESCRIPTION));
			freeTextComplaint.setObsGroup(obsGroupUuidLight);

			allEntities.add(obsGroup);
			allEntities.add(chiefComplaintCoded);
			allEntities.add(freeTextComplaint);
					
		}

		return new Diagnosis(allEntities);
	}

}

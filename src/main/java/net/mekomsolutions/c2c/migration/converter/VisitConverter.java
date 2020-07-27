package net.mekomsolutions.c2c.migration.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import net.mekomsolutions.c2c.migration.Constants;
import net.mekomsolutions.c2c.migration.Utils;
import net.mekomsolutions.c2c.migration.entity.Visit;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEncounter;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntity;
import net.mekomsolutions.c2c.migration.entity.sync.SyncEntityUtils;
import net.mekomsolutions.c2c.migration.entity.sync.SyncObservation;
import net.mekomsolutions.c2c.migration.entity.sync.SyncVisit;

@Converter
public class VisitConverter {

	private static final String START_TIME = "starttime";
	private static final String END_TIME = "endtime";
	private static final String TEMPERATURE = "temperature";
	private static final String WEIGHT = "weight";
	private static final String HEART_RATE = "heartrate";
	private static final String RESPIRATORY_RATE = "respiratoryrate";
	private static final String BP_DIASTOLIC = "bpdiastolic";
	private static final String BP_SYSTOLIC = "bpsystolic";
	
	/**
	 * Transform the data input, passed as a parameter, into the appropriate sub entities.
	 * @throws Exception 
	 * 
	 * @see net.mekomsolutions.c2c.migration.entity.Visit
	 */
	@Converter
	public Visit toVisit(Map<String,String> data , Exchange exchange) throws Exception {

		List<SyncEntity> allEntities = new ArrayList<>();

		UUID visitUuid = UUID.nameUUIDFromBytes(data.get(Constants.OBJECT_KEY).getBytes());
		String patientLight = Utils.getModelClassLight("Patient", 
				UUID.nameUUIDFromBytes(data.get(Constants.PATIENT_KEY).getBytes()));

		// Visit
		String visitTypeLight = Utils.getModelClassLight("VisitType", 
				UUID.fromString(exchange.getContext().resolvePropertyPlaceholders("{{visitType.uuid}}")));
		String visitLight = Utils.getModelClassLight("Visit", visitUuid);
		{	
			SyncVisit visit = new SyncVisit(data, exchange);
			visit.setUuid(visitUuid.toString());
			visit.setDateStarted(Utils.dateStringToArray(data.get(START_TIME)));
			visit.setDateStopped(Utils.dateStringToArray(data.get(END_TIME)));
			visit.setPatient(patientLight);
			visit.setVisitType(visitTypeLight);
			visit.setLocation(Utils.getModelClassLight("Location", 
					UUID.fromString(exchange.getContext().resolvePropertyPlaceholders("{{location.uuid}}"))));
			allEntities.add(visit);
		}

		// Encounter
		String encounterTypeUuid = exchange.getContext().resolvePropertyPlaceholders("{{encounterType.consultation.uuid}}");
		String encounterUuid = SyncEntityUtils.computeNewUUID("", encounterTypeUuid, data);
		String encounterTypeLight = Utils.getModelClassLight("EncounterType", UUID.fromString(encounterTypeUuid));
		String encounterLight = Utils.getModelClassLight("Encounter", UUID.fromString(encounterUuid));
		{
			SyncEncounter encounter = new SyncEncounter(data, exchange);
			encounter.setUuid(encounterUuid);
			encounter.setEncounterType(encounterTypeLight);
			encounter.setPatient(patientLight);
			encounter.setVisit(visitLight);
			// Assuming the encounterDatetime is the Visit start time
			encounter.setEncounterDatetime(Utils.dateStringToArray(data.get(START_TIME)));
			encounter.setLocation(Utils.getModelClassLight("Location", 
					UUID.fromString(exchange.getContext().resolvePropertyPlaceholders("{{location.uuid}}"))));
			allEntities.add(encounter);
		}

		// Temperature obs
		{
			String obsValue = exchange.getContext()
					.getTypeConverter().convertTo(String.class, data.get(TEMPERATURE));
			if (obsValue != null) {
				SyncObservation obs = createObs(data, exchange, encounterLight, patientLight, TEMPERATURE, obsValue);
				obs.setValueNumeric(obsValue);
				allEntities.add(obs);	
			}
		}

		// Weight obs
		{
			String obsValue = exchange.getContext()
					.getTypeConverter().convertTo(String.class, data.get(WEIGHT));
			if (obsValue != null) {
				SyncObservation obs = createObs(data, exchange, encounterLight, patientLight, WEIGHT, obsValue);
				obs.setValueNumeric(obsValue);
				allEntities.add(obs);
			}

		}

		// Heart Rate obs
		{
			String obsValue = exchange.getContext()
					.getTypeConverter().convertTo(String.class, data.get(HEART_RATE));
			if (obsValue != null) {
				SyncObservation obs = createObs(data, exchange, encounterLight, patientLight, HEART_RATE, obsValue);
				obs.setValueNumeric(obsValue);
				allEntities.add(obs);
			}

		}
		// Respiratory Rate obs
		{
			String obsValue = exchange.getContext()
					.getTypeConverter().convertTo(String.class, data.get(RESPIRATORY_RATE));
			if (obsValue != null) {
				SyncObservation obs = createObs(data, exchange, encounterLight, patientLight, RESPIRATORY_RATE, obsValue);
				obs.setValueNumeric(obsValue);
				allEntities.add(obs);
			}

		}
		// BP Diastolic obs
		{
			String obsValue = exchange.getContext()
					.getTypeConverter().convertTo(String.class, data.get(BP_DIASTOLIC));
			if (obsValue != null) {
				SyncObservation obs = createObs(data, exchange, encounterLight, patientLight, BP_DIASTOLIC, obsValue);
				obs.setValueNumeric(obsValue);
				allEntities.add(obs);
			}

		}
		// BP Systolic obs
		{
			String obsValue = exchange.getContext()
					.getTypeConverter().convertTo(String.class, data.get(BP_SYSTOLIC));
			if (obsValue != null) {
				SyncObservation obs = createObs(data, exchange, encounterLight, patientLight, BP_SYSTOLIC, obsValue);
				obs.setValueNumeric(obsValue);
				allEntities.add(obs);
			}

		}

		return new Visit(allEntities);
	}

	private SyncObservation createObs(Map <String, String> data, 
			Exchange exchange, String encounterLight, String patientLight,
			String conceptName, String obsValue) throws Exception {
		
		SyncObservation obs = new SyncObservation(data, exchange);
		String conceptUuid = exchange.getContext().resolvePropertyPlaceholders("{{concept." + conceptName + ".uuid}}");
		obs.setUuid(SyncEntityUtils.computeNewUUID(obsValue, conceptUuid, data));
		// Assuming the obsDatetime is the Visit start time
		obs.setObsDatetime(Utils.dateStringToArray(data.get(START_TIME)));
		obs.setConcept(Utils.getModelClassLight("Concept", conceptUuid));
		obs.setEncounter(encounterLight);
		obs.setPerson(patientLight);
		obs.setLocation(Utils.getModelClassLight("Location", 
				UUID.fromString(exchange.getContext().resolvePropertyPlaceholders("{{location.uuid}}"))));
		obs.setStatus("FINAL");
		return obs;

	}
}

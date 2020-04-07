package net.mekomsolutions.c2c.migration.entity.sync;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.mekomsolutions.c2c.migration.Utils;

/**
 * A Visit ready to be marshaled as a message
 * compatible with the OpenMRS Camel component.
 * 
 */
public class SyncObservation extends SyncEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("personUuid")
	private String person;

	@JsonProperty("conceptUuid")
	private String concept;

	@JsonProperty("encounterUuid")
	private String encounter;

	@JsonProperty("orderUuid")
	private String order;

	@JsonProperty
	private List<Integer> obsDatetime;

	@JsonProperty("locationUuid")
	private String location;

	@JsonProperty("obsGroupUuid")
	private String obsGroup;

	@JsonProperty
	private String accessionNumber;

	@JsonProperty
	private String valueGroupId;

	@JsonProperty("valueCodedUuid")
	private String valueCoded;

	@JsonProperty("valueCodedNameUuid")
	private String valueCodedName;

	@JsonProperty("valueDrugUuid")
	private String valueDrug;

	@JsonProperty
	private List<Integer> valueDatetime;

	@JsonProperty
	private String valueNumeric;

	@JsonProperty
	private Integer valueModifier;

	@JsonProperty
	private String valueText;

	@JsonProperty
	private String valueComplex;

	@JsonProperty
	private String comments;

	@JsonProperty("previousVersionUuid")
	private String previousVersion;

	@JsonProperty
	private String formNamespaceAndPath;

	@JsonProperty
	private String status;

	@JsonProperty
	private String interpretation;

	public SyncObservation(String uuid) {
		super(Utils.getModelClassFullFromType(SyncObservation.class), uuid);
	}

	public SyncObservation(Map<String,String> data, Exchange exchange) throws Exception {
		super(Utils.getModelClassFullFromType(SyncObservation.class), data, exchange);
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getEncounter() {
		return encounter;
	}

	public void setEncounter(String encounter) {
		this.encounter = encounter;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<Integer> getObsDatetime() {
		return obsDatetime;
	}

	public void setObsDatetime(List<Integer> obsDatetime) {
		this.obsDatetime = obsDatetime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getObsGroup() {
		return obsGroup;
	}

	public void setObsGroup(String obsGroup) {
		this.obsGroup = obsGroup;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getValueGroupId() {
		return valueGroupId;
	}

	public void setValueGroupId(String valueGroupId) {
		this.valueGroupId = valueGroupId;
	}

	public String getValueCoded() {
		return valueCoded;
	}

	public void setValueCoded(String valueCoded) {
		this.valueCoded = valueCoded;
	}

	public String getValueCodedName() {
		return valueCodedName;
	}

	public void setValueCodedName(String valueCodedName) {
		this.valueCodedName = valueCodedName;
	}

	public String getValueDrug() {
		return valueDrug;
	}

	public void setValueDrug(String valueDrug) {
		this.valueDrug = valueDrug;
	}

	public List<Integer> getValueDatetime() {
		return valueDatetime;
	}

	public void setValueDatetime(List<Integer> valueDatetime) {
		this.valueDatetime = valueDatetime;
	}

	public String getValueNumeric() {
		return valueNumeric;
	}

	public void setValueNumeric(String valueNumeric) {
		this.valueNumeric = valueNumeric;
	}

	public Integer getValueModifier() {
		return valueModifier;
	}

	public void setValueModifier(Integer valueModifier) {
		this.valueModifier = valueModifier;
	}

	public String getValueText() {
		return valueText;
	}

	public void setValueText(String valueText) {
		this.valueText = valueText;
	}

	public String getValueComplex() {
		return valueComplex;
	}

	public void setValueComplex(String valueComplex) {
		this.valueComplex = valueComplex;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}

	public String getFormNamespaceAndPath() {
		return formNamespaceAndPath;
	}

	public void setFormNamespaceAndPath(String formNamespaceAndPath) {
		this.formNamespaceAndPath = formNamespaceAndPath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

package com.ogya.lokakarya.exercise.feign.response.laporanpenunggakan;

import java.util.HashMap;
import java.util.Map;

public class LaporanPenunggakanResponse {
	private Boolean success;
	private String referenceNumber;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Boolean getSuccess() {
	return success;
	}

	public void setSuccess(Boolean success) {
	this.success = success;
	}

	public String getReferenceNumber() {
	return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
	this.referenceNumber = referenceNumber;
	}

	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}

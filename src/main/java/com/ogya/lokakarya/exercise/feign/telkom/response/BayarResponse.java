package com.ogya.lokakarya.exercise.feign.telkom.response;

import java.util.HashMap;
import java.util.Map;

public class BayarResponse {
	private Boolean success;
	private String referenceNumber;
	private String message;
	private String noRekening;
	private String noTelepon;
	private Integer bulan;
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

	public String getMessage() {
	return message;
	}

	public void setMessage(String message) {
	this.message = message;
	}

	public String getNoRekening() {
	return noRekening;
	}

	public void setNoRekening(String noRekening) {
	this.noRekening = noRekening;
	}

	public String getNoTelepon() {
	return noTelepon;
	}

	public void setNoTelepon(String noTelepon) {
	this.noTelepon = noTelepon;
	}

	public Integer getBulan() {
	return bulan;
	}

	public void setBulan(Integer bulan) {
	this.bulan = bulan;
	}

	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}

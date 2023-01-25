package com.ogya.lokakarya.exercise.feign.request.telkom;

import java.util.HashMap;
import java.util.Map;

public class BayarRequest {
	private Integer bulan;
	private String noRekening;
	private String noTelepon;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getBulan() {
	return bulan;
	}

	public void setBulan(Integer bulan) {
	this.bulan = bulan;
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

	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}

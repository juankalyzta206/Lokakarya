package com.ogya.lokakarya.exercise.feign.response.telkom;

import java.util.HashMap;
import java.util.Map;

public class ValidateResponse {
	private Boolean registered;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Boolean getRegistered() {
	return registered;
	}

	public void setRegistered(Boolean registered) {
	this.registered = registered;
	}

	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}

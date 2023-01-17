package com.ogya.lokakarya.exercise.feign.nasabah.response;

public class NasabahFeignResponse {
	private Boolean success;
	private String referenceNumber;
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
	
}

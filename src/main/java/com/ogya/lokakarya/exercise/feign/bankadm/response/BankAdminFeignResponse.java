package com.ogya.lokakarya.exercise.feign.bankadm.response;

public class BankAdminFeignResponse {
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

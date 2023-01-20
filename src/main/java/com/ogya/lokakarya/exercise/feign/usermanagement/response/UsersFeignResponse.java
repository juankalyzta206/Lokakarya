package com.ogya.lokakarya.exercise.feign.usermanagement.response;

public class UsersFeignResponse {
	String programName;
	Boolean success;

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

}

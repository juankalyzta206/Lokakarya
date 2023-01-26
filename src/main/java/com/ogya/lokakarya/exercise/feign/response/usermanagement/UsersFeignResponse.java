/*
* UsersFeignResponse.java
*	This class is response/wrapper for web service
*
* Version 1.0
*
* Copyright : Irzan Maulana, Backend Team OGYA
*/
package com.ogya.lokakarya.exercise.feign.response.usermanagement;

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

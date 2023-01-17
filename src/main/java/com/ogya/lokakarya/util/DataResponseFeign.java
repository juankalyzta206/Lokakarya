package com.ogya.lokakarya.util;

public class DataResponseFeign<T> {
	private boolean status;
	private String message;
	private Long timeStamp;
	private T data;
	private Boolean success;
	private String referenceNumber;

	public DataResponseFeign() {
		super();
	}

	public DataResponseFeign(T data) {
		this(true, null, data);
	}

	public DataResponseFeign(boolean status, String message) {
		this(status, message, null);
	}

	public DataResponseFeign(boolean status, String message, T data) {
		this.timeStamp = System.currentTimeMillis();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public DataResponseFeign(DataResponseFeign<T> response) {
	    this.status = response.isStatus();
	    this.message = response.getMessage();
	    this.data = response.getData();
	    this.success = response.isSuccess();
	    this.referenceNumber = response.getReferenceNumber();
	}


	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

}

package com.ogya.lokakarya.util;

public class DataResponse<T> {
	
	private boolean status;
	private String message;
	private Long timeStamp;
	private T data;
	
	
	
	public DataResponse() {
		super();
	}

	public DataResponse(T data) {
		this(true, null, data);
	}
	
	public DataResponse(boolean status, String message) {
		this(status, message, null);
	}
	
	public DataResponse(boolean status, String message, T data) {
		this.timeStamp = System.currentTimeMillis();
		this.status = status;
		this.message = message;
		this.data = data;
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
	
	

}


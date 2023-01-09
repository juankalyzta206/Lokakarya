package com.ogya.lokakarya.util;

import java.util.List;

public class DataResponseList<E> {
	
	private boolean status;
	private String message;
	private Long timeStamp;
	private List<E> data;
	
	public DataResponseList(List<E> data) {
		this(true, null, data);
	}
	
	public DataResponseList(boolean status, String message) {
		this(status, message, null);
	}
	
	public DataResponseList(boolean status, String message, List<E> data) {
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
	
	
	public List<E> getData() {
		return data;
	}
	public void setData(List<E> data) {
		this.data = data;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}

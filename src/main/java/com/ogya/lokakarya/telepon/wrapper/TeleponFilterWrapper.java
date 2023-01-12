package com.ogya.lokakarya.telepon.wrapper;

public class TeleponFilterWrapper<T> {
	private String name;
	private T value;

	public String getName() {
	return name;
	}

	public void setName(String name) {
	this.name = name;
	}

	public T getValue() {
	return value;
	}

	public void setValue(T value) {
	this.value = value;
	}
}

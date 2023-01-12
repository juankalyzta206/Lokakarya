package com.ogya.lokakarya.telepon.wrapper;

import java.util.List;
public class TeleponPagingRequestWrapper {
	private Integer offSet;
	private Integer page;
	private Integer size;
	private String sortField;
	private String sortOrder;
	private List<TeleponFilterWrapper> filters = null;

	public Integer getOffSet() {
	return offSet;
	}

	public void setOffSet(Integer offSet) {
	this.offSet = offSet;
	}

	public Integer getPage() {
	return page;
	}

	public void setPage(Integer page) {
	this.page = page;
	}

	public Integer getSize() {
	return size;
	}

	public void setSize(Integer size) {
	this.size = size;
	}
	public String getSortField() {
	return sortField;
	}

	public void setSortField(String sortField) {
	this.sortField = sortField;
	}

	public String getSortOrder() {
	return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
	this.sortOrder = sortOrder;
	}

	public List<TeleponFilterWrapper> getFilters() {
	return filters;
	}

	public void setFilters(List<TeleponFilterWrapper> filters) {
	this.filters = filters;
	}
}

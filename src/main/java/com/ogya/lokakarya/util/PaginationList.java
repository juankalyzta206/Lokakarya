package com.ogya.lokakarya.util;

import java.util.List;

import org.springframework.data.domain.Page;

public class PaginationList<T, U> {
	
	private Integer page;
	private Integer size;
	private Integer totalPages;
	private Long totalRowCount;
	private List<T> data;
	
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
	
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	
	public Long getTotalRowCount() {
		return totalRowCount;
	}
	public void setTotalRowCount(Long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
	public List<T> getData() {
		return data;
	}
	
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public PaginationList(List<T> datalist, Page<U> list) {
		page = list.getNumber();
		size = list.getSize();
		totalPages = list.getTotalPages();
		totalRowCount = list.getTotalElements();
		this.data = datalist;	
	}
	
	
	
	
	
}

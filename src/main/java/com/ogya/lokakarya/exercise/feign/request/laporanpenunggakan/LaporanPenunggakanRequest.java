package com.ogya.lokakarya.exercise.feign.request.laporanpenunggakan;

import java.util.HashMap;
import java.util.Map;

public class LaporanPenunggakanRequest {
	private Integer bulanTunggakan;
	private String namaPelanggan;
	private String status;
	private Integer tagihan;
	private Integer tahunTagihan;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getBulanTunggakan() {
	return bulanTunggakan;
	}

	public void setBulanTunggakan(Integer bulanTunggakan) {
	this.bulanTunggakan = bulanTunggakan;
	}

	public String getNamaPelanggan() {
	return namaPelanggan;
	}

	public void setNamaPelanggan(String namaPelanggan) {
	this.namaPelanggan = namaPelanggan;
	}

	public String getStatus() {
	return status;
	}

	public void setStatus(String status) {
	this.status = status;
	}

	public Integer getTagihan() {
	return tagihan;
	}

	public void setTagihan(Integer tagihan) {
	this.tagihan = tagihan;
	}

	public Integer getTahunTagihan() {
	return tahunTagihan;
	}

	public void setTahunTagihan(Integer tahunTagihan) {
	this.tahunTagihan = tahunTagihan;
	}

	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}

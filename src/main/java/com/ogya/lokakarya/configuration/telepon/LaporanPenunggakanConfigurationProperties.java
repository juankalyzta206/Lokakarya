package com.ogya.lokakarya.configuration.telepon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class LaporanPenunggakanConfigurationProperties {
	private String idTransaksi;
	private String nama;
	private String bulanTagihan;
	private String tahunTagihan;
	private String nominal;
	private String status;	
	public String getIdTransaksi() {
		return idTransaksi;
	}
	public void setIdTransaksi(String idTransaksi) {
		this.idTransaksi = idTransaksi;
	}
	public String getNamaPelanggan() {
		return nama;
	}
	public void setNamaPelanggan(String namaPelanggan) {
		this.nama = namaPelanggan;
	}
	public String getBulanTagihan() {
		return bulanTagihan;
	}
	public void setBulanTagihan(String bulanTagihan) {
		this.bulanTagihan = bulanTagihan;
	}
	public String getTahunTagihan() {
		return tahunTagihan;
	}
	public void setTahunTagihan(String tahunTagihan) {
		this.tahunTagihan = tahunTagihan;
	}
	public String getNominal() {
		return nominal;
	}
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

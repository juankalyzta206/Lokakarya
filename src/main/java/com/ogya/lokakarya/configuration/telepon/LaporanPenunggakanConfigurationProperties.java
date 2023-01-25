package com.ogya.lokakarya.configuration.telepon;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "column")
@PropertySource("classpath:column/columnLaporanPenunggakan.properties")
public class LaporanPenunggakanConfigurationProperties {
	private String idTransaksi;
	private String nama;
	private String bulanTagihan;
	private String tahunTagihan;
	private String nominal;
	private String status;
	@Value("#{'${column.column}'.split(',')}")
	private List<String> column;
	

	public String getIdTransaksi() {
		return idTransaksi;
	}
	public void setIdTransaksi(String idTransaksi) {
		this.idTransaksi = idTransaksi;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
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
	public List<String> getColumn() {
		return column;
	}
	public void setColumn(List<String> column) {
		this.column = column;
	}
}

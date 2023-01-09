package com.ogya.lokakarya.telepon.wrapper;

import java.util.Date;

public class HistoryWrapper {
	private Long idHistory;
	private Date tanggalBayar;
	private Long idPelanggan;
	private Byte bulanTagihan;
	private Integer tahunTagihan;
	private Long uang;
	
	public Long getIdHistory() {
		return idHistory;
	}
	public void setIdHistory(Long idHistory) {
		this.idHistory = idHistory;
	}
	public Date getTanggalBayar() {
		return tanggalBayar;
	}
	public void setTanggalBayar(Date tanggalBayar) {
		this.tanggalBayar = tanggalBayar;
	}
	public Long getIdPelanggan() {
		return idPelanggan;
	}
	public void setIdPelanggan(Long idPelanggan) {
		this.idPelanggan = idPelanggan;
	}
	public Byte getBulanTagihan() {
		return bulanTagihan;
	}
	public void setBulanTagihan(Byte bulanTagihan) {
		this.bulanTagihan = bulanTagihan;
	}
	public Integer getTahunTagihan() {
		return tahunTagihan;
	}
	public void setTahunTagihan(Integer tahunTagihan) {
		this.tahunTagihan = tahunTagihan;
	}
	public Long getUang() {
		return uang;
	}
	public void setUang(Long uang) {
		this.uang = uang;
	}
}

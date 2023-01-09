package com.ogya.lokakarya.bankadm.wrapper;

import java.util.Date;

public class TransferWrapper {
	private Long rekAsal;
	private Long rekTujuan;
	private String namaPengirim;
	private String namaPenerima;
	private Long nominal;
	private Date tanggal;
	private Long saldoPengirim;
	private Long saldoPenerima;
	
	
	public Long getSaldoPengirim() {
		return saldoPengirim;
	}
	public void setSaldoPengirim(Long saldoPengirim) {
		this.saldoPengirim = saldoPengirim;
	}
	public Long getSaldoPenerima() {
		return saldoPenerima;
	}
	public void setSaldoPenerima(Long saldoPenerima) {
		this.saldoPenerima = saldoPenerima;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public Long getRekAsal() {
		return rekAsal;
	}
	public void setRekAsal(Long rekAsal) {
		this.rekAsal = rekAsal;
	}
	public Long getRekTujuan() {
		return rekTujuan;
	}
	public void setRekTujuan(Long rekTujuan) {
		this.rekTujuan = rekTujuan;
	}
	public String getNamaPengirim() {
		return namaPengirim;
	}
	public void setNamaPengirim(String namaPengirim) {
		this.namaPengirim = namaPengirim;
	}
	public String getNamaPenerima() {
		return namaPenerima;
	}
	public void setNamaPenerima(String namaPenerima) {
		this.namaPenerima = namaPenerima;
	}
	public Long getNominal() {
		return nominal;
	}
	public void setNominal(Long nominal) {
		this.nominal = nominal;
	}

	
}

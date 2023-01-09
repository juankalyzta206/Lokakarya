package com.ogya.lokakarya.bankadm.wrapper;

import java.util.Date;

public class SetorAmbilWrapper {
	private Long nomorRekening;
	private String namaNasabah;
	private Long nominal;
	private Long saldo;
	private Date tanggal;
	
	
	public Long getNomorRekening() {
		return nomorRekening;
	}
	public void setNomorRekening(Long nomorRekening) {
		this.nomorRekening = nomorRekening;
	}
	public String getNamaNasabah() {
		return namaNasabah;
	}
	public void setNamaNasabah(String namaNasabah) {
		this.namaNasabah = namaNasabah;
	}
	public Long getNominal() {
		return nominal;
	}
	public void setNominal(Long nominal) {
		this.nominal = nominal;
	}
	public Long getSaldo() {
		return saldo;
	}
	public void setSaldo(Long saldo) {
		this.saldo = saldo;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	
	

}

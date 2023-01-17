package com.ogya.lokakarya.exercise.feign.bankadm.request;

public class BankAdminFeignRequest {
	private String alamat;
	private String nama;
	private Long nominalSaldo;
	private String telpon;

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Long getNominalSaldo() {
		return nominalSaldo;
	}

	public void setNominalSaldo(Long nominalSaldo) {
		this.nominalSaldo = nominalSaldo;
	}

	public String getTelpon() {
		return telpon;
	}

	public void setTelpon(String telpon) {
		this.telpon = telpon;
	}

}

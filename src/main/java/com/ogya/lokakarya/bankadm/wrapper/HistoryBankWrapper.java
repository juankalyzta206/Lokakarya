package com.ogya.lokakarya.bankadm.wrapper;

import java.util.Date;

public class HistoryBankWrapper {
	private Long idHistoryBank;
	private Long norek;
	private String nama;
	private Date tanggal;
	private Long uang;
	private Byte statusKet;
	private Long NoRekTujuan;
	private Long noTlp;
	
	public Long getIdHistoryBank() {
		return idHistoryBank;
	}
	public void setIdHistoryBank(Long idHistoryBank) {
		this.idHistoryBank = idHistoryBank;
	}
	public Long getNorek() {
		return norek;
	}
	public void setNorek(Long norek) {
		this.norek = norek;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public Long getUang() {
		return uang;
	}
	public void setUang(Long uang) {
		this.uang = uang;
	}
	public Byte getStatusKet() {
		return statusKet;
	}
	public void setStatusKet(Byte statusKet) {
		this.statusKet = statusKet;
	}
	public Long getNoRekTujuan() {
		return NoRekTujuan;
	}
	public void setNoRekTujuan(Long noRekTujuan) {
		NoRekTujuan = noRekTujuan;
	}
	public Long getNoTlp() {
		return noTlp;
	}
	public void setNoTlp(Long noTlp) {
		this.noTlp = noTlp;
	}
	@Override
	public String toString() {
		return "HistoryBankWrapper [idHistoryBank=" + idHistoryBank + ", norek=" + norek + ", nama=" + nama
				+ ", tanggel=" + tanggal + ", uang=" + uang + ", statusKet=" + statusKet + ", NoRekTujuan="
				+ NoRekTujuan + ", noTlp=" + noTlp + "]";
	}

	
	
}

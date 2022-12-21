package com.ogya.lokakarya.wrapper;

import java.util.Date;

public class HistoryBankWrapper {
	private Long idHistoryBank;
	private Long norek;
	private String nama;
	private Date tanggel;
	private Long uang;
	private Long statusKet;
	private Long NoRekTujuan;
	private Long no_tlp;
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
	public Date getTanggel() {
		return tanggel;
	}
	public void setTanggel(Date tanggel) {
		this.tanggel = tanggel;
	}
	public Long getUang() {
		return uang;
	}
	public void setUang(Long uang) {
		this.uang = uang;
	}
	public Long getStatusKet() {
		return statusKet;
	}
	public void setStatusKet(Long statusKet) {
		this.statusKet = statusKet;
	}
	public Long getNoRekTujuan() {
		return NoRekTujuan;
	}
	public void setNoRekTujuan(Long noRekTujuan) {
		NoRekTujuan = noRekTujuan;
	}
	public Long getNo_tlp() {
		return no_tlp;
	}
	public void setNo_tlp(Long no_tlp) {
		this.no_tlp = no_tlp;
	}
	@Override
	public String toString() {
		return "HistoryBankWrapper [idHistoryBank=" + idHistoryBank + ", norek=" + norek + ", nama=" + nama
				+ ", tanggel=" + tanggel + ", uang=" + uang + ", statusKet=" + statusKet + ", NoRekTujuan="
				+ NoRekTujuan + ", no_tlp=" + no_tlp + "]";
	}

	
	
}

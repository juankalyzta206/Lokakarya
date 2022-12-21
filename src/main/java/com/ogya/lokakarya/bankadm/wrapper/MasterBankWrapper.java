package com.ogya.lokakarya.bankadm.wrapper;

public class MasterBankWrapper {
	private Long norek;
	private String nama;
	private String alamat;
	private Long notlp;
	private Long saldo;
	private Long userId;
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
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public Long getNotlp() {
		return notlp;
	}
	public void setNotlp(Long notlp) {
		this.notlp = notlp;
	}
	public Long getSaldo() {
		return saldo;
	}
	public void setSaldo(Long saldo) {
		this.saldo = saldo;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "MasterBankWrapper [norek=" + norek + ", nama=" + nama + ", alamat=" + alamat + ", notlp=" + notlp
				+ ", saldo=" + saldo + ", userId=" + userId + "]";
	}
	
}

package com.ogya.lokakarya.telepon.wrapper;

public class MasterPelangganWrapper {
	private Long idPelanggan;
	private String nama;
	private Long noTelp;
	private String alamat;
	private Long userId;
	
	public Long getIdPelanggan() {
		return idPelanggan;
	}
	public void setIdPelanggan(Long idPelanggan) {
		this.idPelanggan = idPelanggan;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Long getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(Long noTelp) {
		this.noTelp = noTelp;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "MasterPelangganWrapper [idPelanggan=" + idPelanggan + ", nama=" + nama + ", noTelp=" + noTelp
				+ ", alamat=" + alamat + ", userId=" + userId + "]";
	}
	
}

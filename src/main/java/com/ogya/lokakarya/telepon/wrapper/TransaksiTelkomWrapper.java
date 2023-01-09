package com.ogya.lokakarya.telepon.wrapper;

public class TransaksiTelkomWrapper {
	private Long idTransaksi;
	private Long idPelanggan;
	private Byte bulanTagihan;
	private Integer tahunTagihan;
	private Long uang;
	private Byte status;
	public Long getIdTransaksi() {
		return idTransaksi;
	}
	public void setIdTransaksi(Long idTransaksi) {
		this.idTransaksi = idTransaksi;
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
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
}

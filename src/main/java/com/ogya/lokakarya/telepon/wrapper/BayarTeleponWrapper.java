package com.ogya.lokakarya.telepon.wrapper;

import java.util.Date;

public class BayarTeleponWrapper {
	private Long idPelanggan;
	private String namaPelanggan;
	private Long noTelepon;
	private Long tagihan;
	private Long noRekening;
	private Long idTransaksi;
	private Byte bulanTagihan;
	private Integer tahunTagihan;
	private Byte status;
	private Long saldo;
	private Date tanggal;
	
	
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public Long getSaldo() {
		return saldo;
	}
	public void setSaldo(Long saldo) {
		this.saldo = saldo;
	}
	public Long getIdTransaksi() {
		return idTransaksi;
	}
	public void setIdTransaksi(Long idTransaksi) {
		this.idTransaksi = idTransaksi;
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
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	private String namaRekening;
	public Long getIdPelanggan() {
		return idPelanggan;
	}
	public void setIdPelanggan(Long idPelanggan) {
		this.idPelanggan = idPelanggan;
	}
	public String getNamaPelanggan() {
		return namaPelanggan;
	}
	public void setNamaPelanggan(String namaPelanggan) {
		this.namaPelanggan = namaPelanggan;
	}
	public Long getNoTelepon() {
		return noTelepon;
	}
	public void setNoTelepon(Long noTelepon) {
		this.noTelepon = noTelepon;
	}
	public Long getTagihan() {
		return tagihan;
	}
	public void setTagihan(Long tagihan) {
		this.tagihan = tagihan;
	}
	public Long getNoRekening() {
		return noRekening;
	}
	public void setNoRekening(Long noRekening) {
		this.noRekening = noRekening;
	}
	public String getNamaRekening() {
		return namaRekening;
	}
	public void setNamaRekening(String namaRekening) {
		this.namaRekening = namaRekening;
	}
	
}

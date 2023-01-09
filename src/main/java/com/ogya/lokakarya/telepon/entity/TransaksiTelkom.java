package com.ogya.lokakarya.telepon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSAKSI_TELKOM")
public class TransaksiTelkom {
	private Long idTransaksi;
	private MasterPelanggan idPelanggan;
	private Byte bulanTagihan;
	private Integer tahunTagihan;
	private Long uang;
	private Byte status;
	@Id
	@GeneratedValue(generator = "TRANSAKSI_TELKOM_GEN" , strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TRANSAKSI_TELKOM_GEN", sequenceName = "TRANSAKSI_TELKOM_SEQ", initialValue = 5, allocationSize = 1)
	public Long getIdTransaksi() {
		return idTransaksi;
	}
	public void setIdTransaksi(Long idTransaksi) {
		this.idTransaksi = idTransaksi;
	}
	@ManyToOne
	@JoinColumn(name = "ID_PELANGGAN")
	public MasterPelanggan getIdPelanggan() {
		return idPelanggan;
	}
	public void setIdPelanggan(MasterPelanggan idPelanggan) {
		this.idPelanggan = idPelanggan;
	}
	@Column(name = "BULAN_TAGIHAN")
	public Byte getBulanTagihan() {
		return bulanTagihan;
	}
	public void setBulanTagihan(Byte bulanTagihan) {
		this.bulanTagihan = bulanTagihan;
	}
	@Column(name = "TAHUN_TAGIHAN")
	public Integer getTahunTagihan() {
		return tahunTagihan;
	}
	public void setTahunTagihan(Integer tahunTagihan) {
		this.tahunTagihan = tahunTagihan;
	}
	@Column(name = "UANG")
	public Long getUang() {
		return uang;
	}
	public void setUang(Long uang) {
		this.uang = uang;
	}
	@Column(name = "STATUS")
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
}

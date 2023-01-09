package com.ogya.lokakarya.telepon.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HISTORY_TELKOM")
public class HistoryTelkom {
	private Long idHistory;
	private MasterPelanggan idPelanggan;
	private Date tanggalBayar;
	private Byte bulanTagihan;
	private Integer tahunTagihan;
	private Long uang;
	@Id
	@GeneratedValue(generator = "HISTORY_TELKOM_GEN" , strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "HISTORY_TELKOM_GEN", sequenceName = "HISTORY_TELKOM_SEQ", initialValue = 1, allocationSize = 1)
	public Long getIdHistory() {
		return idHistory;
	}
	public void setIdHistory(Long idHistory) {
		this.idHistory = idHistory;
	}
	@ManyToOne
	@JoinColumn(name = "ID_PELANGGAN")
	public MasterPelanggan getIdPelanggan() {
		return idPelanggan;
	}
	public void setIdPelanggan(MasterPelanggan idPelanggan) {
		this.idPelanggan = idPelanggan;
	}
	@Column(name = "TANGGAL_BAYAR")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTanggalBayar() {
		return tanggalBayar;
	}
	public void setTanggalBayar(Date tanggalBayar) {
		this.tanggalBayar = tanggalBayar;
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
	
	@PrePersist
	private void onCreate() {
	    tanggalBayar = new Date();
	}
}

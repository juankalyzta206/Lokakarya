package com.ogya.lokakarya.bankadm.entity;

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
@Table(name="TRANSAKSI_NASABAH")
public class TransaksiNasabah {
	private Long idTransaksiNasabah;
	private MasterBank masterBank;
	private Date tanggal;
	private String status;
	private Long uang;
	private Byte statusKet;
	private Long norekDituju;
	private Long noTlp;
	
	@Id
	@GeneratedValue(generator = "TRANSAKSI_NASABAH_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TRANSAKSI_NASABAH_GEN", sequenceName = "TRANSFER_NASABAH_SEQ", initialValue = 1, allocationSize = 1)
	public Long getIdTransaksiNasabah() {
		return idTransaksiNasabah;
	}
	public void setIdTransaksiNasabah(Long idTransaksiNasabah) {
		this.idTransaksiNasabah = idTransaksiNasabah;
	}
	
	@ManyToOne
	@JoinColumn(name="NOREK")
	public MasterBank getMasterBank() {
		return masterBank;
	}
	public void setMasterBank(MasterBank masterBank) {
		this.masterBank = masterBank;
	}
	
	@Column(name="TANGGAL")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="UANG")
	public Long getUang() {
		return uang;
	}
	public void setUang(Long uang) {
		this.uang = uang;
	}
	
	@Column(name="STATUS_KET")
	public Byte getStatusKet() {
		return statusKet;
	}
	public void setStatusKet(Byte statusKet) {
		this.statusKet = statusKet;
	}
	
	@Column(name="NOREK_DITUJU")
	public Long getNorekDituju() {
		return norekDituju;
	}
	public void setNorekDituju(Long norekDituju) {
		this.norekDituju = norekDituju;
	}
	
	@Column(name="NO_TLP")
	public Long getNoTlp() {
		return noTlp;
	}
	public void setNoTlp(Long noTlp) {
		this.noTlp = noTlp;
	}

	@PrePersist
	private void onCreate() {
		tanggal = new Date();
	}
	
}

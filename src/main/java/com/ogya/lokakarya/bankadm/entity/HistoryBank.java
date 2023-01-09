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
@Table(name = "HISTORY_BANK")
public class HistoryBank {
	
	private Long idHistoryBank;
	private MasterBank rekening;
	private String nama;
	private Date tanggal;
	private Long uang;
	private Byte statusKet;
	private Long NoRekTujuan;
	private Long noTlp;
	
	@Id
	@GeneratedValue(generator = "HISTORY_BANK_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "HISTORY_BANK_GEN", sequenceName = "HISTORY_BANK_SEQ", initialValue = 1, allocationSize = 1)

	public Long getIdHistoryBank() {
		return idHistoryBank;
	}
	public void setIdHistoryBank(Long idHistoryBank) {
		this.idHistoryBank = idHistoryBank;
	}
	
	@Column(name = "TANGGAL")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggel) {
		this.tanggal = tanggel;
	}
	
	@ManyToOne
	@JoinColumn(name = "norek")
	public MasterBank getRekening() {
		return rekening;
	}
	public void setRekening(MasterBank rekening) {
		this.rekening = rekening;
	}
	
	
	@Column(name = "NAMA")
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	@Column(name = "uang")
	public Long getUang() {
		return uang;
	}
	public void setUang(Long uang) {
		this.uang = uang;
	}
	
	@Column(name = "STATUS_KET")
	public Byte getStatusKet() {
		return statusKet;
	}
	public void setStatusKet(Byte statusKet) {
		this.statusKet = statusKet;
	}
	
	@Column(name = "NOREK_DITUJU")
	public Long getNoRekTujuan() {
		return NoRekTujuan;
	}
	public void setNoRekTujuan(Long noRekTujuan) {
		NoRekTujuan = noRekTujuan;
	}
	
	@Column(name = "NO_TLP")
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

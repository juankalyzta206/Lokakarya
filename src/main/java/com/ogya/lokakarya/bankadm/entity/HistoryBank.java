package com.ogya.lokakarya.bankadm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private Date tanggel;
	private Long uang;
	private Long statusKet;
	private Long NoRekTujuan;
	private Long no_tlp;
	
	@Id
	@GeneratedValue(generator = "HISTORYBANK_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "HISTORYBANK_GEN", sequenceName = "HISTORYBANK_SEQ", initialValue = 1, allocationSize = 1)

	public Long getIdHistoryBank() {
		return idHistoryBank;
	}
	public void setIdHistoryBank(Long idHistoryBank) {
		this.idHistoryBank = idHistoryBank;
	}
	
	@Column(name = "TANGGAL")
	@Temporal(TemporalType.DATE)
	public Date getTanggel() {
		return tanggel;
	}
	public void setTanggel(Date tanggel) {
		this.tanggel = tanggel;
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
	public Long getStatusKet() {
		return statusKet;
	}
	public void setStatusKet(Long statusKet) {
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
	public Long getNo_tlp() {
		return no_tlp;
	}
	public void setNo_tlp(Long no_tlp) {
		this.no_tlp = no_tlp;
	}
	
	
	
}

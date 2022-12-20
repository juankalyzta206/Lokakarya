package com.ogya.lokakarya.entity;

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
@Table(name = "transaksi_nasabah")
public class TransaksiNasabah {
	private Long idTransaksiNasabah;
	private MasterBank rekening;
	private Date tanggel;
	private String status;
	private Long uang;
	private Long statusKet;
	private Long NoRekTujuan;
	private Long no_tlp;
	
	@Id
	@GeneratedValue(generator = "TRANSAKSINASABAH_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TRANSAKSINASABAH_GEN", sequenceName = "TRANSAKSINASABAH_SEQ", initialValue = 1, allocationSize = 1)

	
	public Long getIdTransaksiNasabah() {
		return idTransaksiNasabah;
	}
	public void setIdTransaksiNasabah(Long idTransaksiNasabah) {
		this.idTransaksiNasabah = idTransaksiNasabah;
	}
	
	@ManyToOne
	@JoinColumn(name = "norek")
	public MasterBank getRekening() {
		return rekening;
	}
	public void setRekening(MasterBank rekening) {
		this.rekening = rekening;
	}
	
	@Column(name = "TANGGAL")
	@Temporal(TemporalType.DATE)
	public Date getTanggel() {
		return tanggel;
	}
	public void setTanggel(Date tanggel) {
		this.tanggel = tanggel;
	}
	
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "UANG")
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

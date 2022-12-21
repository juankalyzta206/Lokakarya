package com.ogya.lokakarya.bankadm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "master_bank")
public class MasterBank {
private Long norek;
private String nama;
private String alamat;
private Long notlp;
private Long saldo;
private Long userId;

@Id
@GeneratedValue(generator = "MASTERBANK_GEN", strategy = GenerationType.SEQUENCE)
@SequenceGenerator(name = "MASTERBANK_GEN", sequenceName = "MASTERBANK_SEQ", initialValue = 1, allocationSize = 1)

public Long getNorek() {
	return norek;
}
public void setNorek(Long norek) {
	this.norek = norek;
}

@Column(name = "NAMA")
public String getNama() {
	return nama;
}
public void setNama(String nama) {
	this.nama = nama;
}

@Column(name = "ALAMAT")
public String getAlamat() {
	return alamat;
}
public void setAlamat(String alamat) {
	this.alamat = alamat;
}

@Column(name = "NOTLP")
public Long getNotlp() {
	return notlp;
}
public void setNotlp(Long notlp) {
	this.notlp = notlp;
}

@Column(name = "SALDO")
public Long getSaldo() {
	return saldo;
}
public void setSaldo(long saldo) {
	this.saldo = saldo;
}

@Column(name = "USER_ID")
public Long getUserId() {
	return userId;
}
public void setUserId(Long userId) {
	this.userId = userId;
}


}

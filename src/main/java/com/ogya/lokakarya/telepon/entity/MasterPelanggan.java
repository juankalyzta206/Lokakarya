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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogya.lokakarya.usermanagement.entity.Users;

@Entity
@Table(name = "MASTER_PELANGGAN")
public class MasterPelanggan {
	private Long idPelanggan;
	private String nama;
	private Long noTelp;
	private String alamat;
	private Users users;
	
	@Id
	@GeneratedValue(generator = "ID_PELANGGAN_MST_GEN" , strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ID_PELANGGAN_MST_GEN", sequenceName = "ID_PELANGGAN_MST", initialValue = 2, allocationSize = 1)
	public Long getIdPelanggan() {
		return idPelanggan;
	}
	public void setIdPelanggan(Long idPelanggan) {
		this.idPelanggan = idPelanggan;
	}
	@Column(name = "NAMA")
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	@Column(name = "NO_TELP")
	public Long getNoTelp() {
		return noTelp;
	}
	public void setNoTelp(Long noTelp) {
		this.noTelp = noTelp;
	}
	@Column(name = "ALAMAT")
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	
}

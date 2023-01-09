package com.ogya.lokakarya.usermanagement.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "USERS")
public class Login  {
	private Long userId;
	private String username;
	private String password;
	private String nama;
	private String alamat;
	private String email;
	private String telp;
	private String programName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private Set<HakAkses> hakAkses = new HashSet<HakAkses>(0);
	
	@Id
	@GeneratedValue(generator = "USER_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "USER_GEN", sequenceName = "USERS_SEQ", initialValue = 1, allocationSize = 1)
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "NAMA")
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "ALAMAT")
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "TELP")
	public String getTelp() {
		return telp;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "PROGRAM_NAME")
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.DATE)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "UPDATED_DATE")
	@Temporal(TemporalType.DATE)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	
	//--------------------------------------------------------------------------------------------------------

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
	@JsonIgnore
	public Set<HakAkses> getHakAkses() {
		return this.hakAkses;
	}

	public void setHakAkses(Set<HakAkses> hakAkses) {
		this.hakAkses = hakAkses;
	}
	
	

}


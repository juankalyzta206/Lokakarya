package com.ogya.lokakarya.usermanagement.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "HAK_AKSES")
public class HakAkses  {
	private Long hakAksesId;
	private Login users;
	private Roles roles;
	private String programName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	
	
	@Id
	@GeneratedValue(generator = "HAK_AKSES_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "HAK_AKSES_GEN", sequenceName = "HAK_AKSES_SEQ", initialValue = 1, allocationSize = 1)
	public Long getHakAksesId() {
		return hakAksesId;
	}
	public void setHakAksesId(Long hakAksesId) {
		this.hakAksesId = hakAksesId;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	public Login getUsers() {
		return users;
	}
	public void setUsers(Login users) {
		this.users = users;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	public Roles getRoles() {
		return roles;
	}
	public void setRoles(Roles roles) {
		this.roles = roles;
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
	
	
	}





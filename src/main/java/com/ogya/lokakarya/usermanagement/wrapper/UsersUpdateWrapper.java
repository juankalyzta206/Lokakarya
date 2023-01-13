package com.ogya.lokakarya.usermanagement.wrapper;

import java.util.Date;


public class UsersUpdateWrapper {
	private Long userId;
	private String username;
	private String nama;
	private String alamat;
	private String email;
	private Long telp;
	private String programName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private Integer sameUsername;
	private Integer sameEmail;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
	//--------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------

	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	//--------------------------------------------------------------------------------------------------------

	public Long getTelp() {
		return telp;
	}
	public void setTelp(Long telp) {
		this.telp = telp;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}	
	//--------------------------------------------------------------------------------------------------------

	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}	
	//--------------------------------------------------------------------------------------------------------

	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	public Integer getSameUsername() {
		return sameUsername;
	}
	public void setSameUsername(Integer sameUsername) {
		this.sameUsername = sameUsername;
	}
	public Integer getSameEmail() {
		return sameEmail;
	}
	public void setSameEmail(Integer sameEmail) {
		this.sameEmail = sameEmail;
	}
	@Override
	public String toString() {
		return "UsersUpdateWrapper [userId=" + userId + ", username=" + username + ", nama=" + nama + ", alamat="
				+ alamat + ", email=" + email + ", telp=" + telp + ", programName=" + programName + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy
				+ ", sameUsername=" + sameUsername + ", sameEmail=" + sameEmail + "]";
	}

	
	
	
	
	
	
	
}

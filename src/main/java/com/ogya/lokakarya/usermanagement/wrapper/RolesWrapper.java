package com.ogya.lokakarya.usermanagement.wrapper;

import java.util.Date;
import java.util.Set;

import com.ogya.lokakarya.usermanagement.entity.RoleMenu;


public class RolesWrapper {
	private Long roleId;
	private String nama;
	private String programName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private Set<RoleMenu> roleMenu;
	

	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}//--------------------------------------------------------------------------------------------------------



	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}//--------------------------------------------------------------------------------------------------------



	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}//--------------------------------------------------------------------------------------------------------



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
	
	
	public Set<RoleMenu> getRoleMenu() {
		return roleMenu;
	}
	public void setRoleMenu(Set<RoleMenu> roleMenu) {
		this.roleMenu = roleMenu;
	}
	//--------------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "RolesWrapper [roleId=" + roleId + ", nama=" + nama + ", programName=" + programName + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy
				+ "]";
	}
	
	
	
	
	
	
}

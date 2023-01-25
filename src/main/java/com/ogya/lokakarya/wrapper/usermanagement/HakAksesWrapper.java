package com.ogya.lokakarya.wrapper.usermanagement;

import java.util.Date;

public class HakAksesWrapper {
	private Long hakAksesId;
	private Long userId;
	private String username;
	private Long roleId;
	private String roleName;
	private String programName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;

	public Long getHakAksesId() {
		return hakAksesId;
	}

	public void setHakAksesId(Long hakAksesId) {
		this.hakAksesId = hakAksesId;
	}
	// --------------------------------------------------------------------------------------------------------

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	// --------------------------------------------------------------------------------------------------------

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	// --------------------------------------------------------------------------------------------------------

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	// --------------------------------------------------------------------------------------------------------

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	// --------------------------------------------------------------------------------------------------------

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	// --------------------------------------------------------------------------------------------------------

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	// --------------------------------------------------------------------------------------------------------

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	// --------------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "HakAksesWrapper [hakAksesId=" + hakAksesId + ", userId=" + userId + ", username=" + username
				+ ", roleId=" + roleId + ", roleName=" + roleName + ", programName=" + programName + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy
				+ "]";
	}

}

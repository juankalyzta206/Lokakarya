package com.ogya.lokakarya.usermanagement.wrapper;

import java.util.Date;
import java.util.Set;

import com.ogya.lokakarya.usermanagement.entity.RoleMenu;


public class RolesLoginWrapper {
	private Long roleId;
	private String nama;
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
	
	
	public Set<RoleMenu> getRoleMenu() {
		return roleMenu;
	}
	public void setRoleMenu(Set<RoleMenu> roleMenu) {
		this.roleMenu = roleMenu;
	}
	//--------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "RolesLoginWrapper [roleId=" + roleId + ", nama=" + nama + ", roleMenu=" + roleMenu + "]";
	}
	

	
}

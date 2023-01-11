package com.ogya.lokakarya.usermanagement.entity.login;


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
import com.ogya.lokakarya.usermanagement.entity.Roles;

@Entity
@Table(name = "ROLE_MENU")
public class RoleMenuLogin {
	private Long roleMenuId;
	private Roles roles;
	private MenuLogin menu;
	private String isActive;
	
	
	@Id
	@GeneratedValue(generator = "ROLE_MENU_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ROLE_MENU_GEN", sequenceName = "ROLE_MENU_SEQ", initialValue = 1, allocationSize = 1)
	public Long getRoleMenuId() {
		return roleMenuId;
	}
	public void setRoleMenuId(Long roleMenuId) {
		this.roleMenuId = roleMenuId;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	@JsonIgnore
	public Roles getRoles() {
		return roles;
	}
	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	//--------------------------------------------------------------------------------------------------------
	@ManyToOne
	@JoinColumn(name = "MENU_ID")
	public MenuLogin getMenu() {
		return menu;
	}
	public void setMenu(MenuLogin menu) {
		this.menu = menu;
	}
		
	//--------------------------------------------------------------------------------------------------------
	@Column(name = "IS_ACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
	}





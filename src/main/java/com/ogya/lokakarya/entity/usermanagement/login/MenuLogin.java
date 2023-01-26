/*
* MenuLogin.java
*	This class is menu entity/table but only for login, getter and setter for each column
*
* Version 1.0
*
* Copyright : Irzan Maulana, Backend Team OGYA
*/
package com.ogya.lokakarya.entity.usermanagement.login;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "MENU")
public class MenuLogin {
	private Long menuId;
	private String nama;
	private String icon;
	private String url;
	private Set<SubMenuLogin> subMenu = new HashSet<SubMenuLogin>(0);

	@Id
	@GeneratedValue(generator = "MENU_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "MENU_GEN", sequenceName = "MENU_SEQ", initialValue = 1, allocationSize = 1)
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "NAMA")
	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	// --------------------------------------------------------------------------------------------------------
	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@OneToMany(mappedBy = "menu")
	public Set<SubMenuLogin> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(Set<SubMenuLogin> subMenu) {
		this.subMenu = subMenu;
	}

}

/*
* SubMenuLogin.java
*	This class is sub menu entity/table but only for login, getter and setter for each column
*
* Version 1.0
*
* Copyright : Irzan Maulana, Backend Team OGYA
*/
package com.ogya.lokakarya.entity.usermanagement.login;

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
@Table(name = "SUB_MENU")
public class SubMenuLogin {
	private Long subMenuId;
	private String nama;
	private String icon;
	private String url;
	private MenuLogin menu;

	@Id
	@GeneratedValue(generator = "SUB_MENU_GEN", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SUB_MENU_GEN", sequenceName = "SUB_MENU_SEQ", initialValue = 1, allocationSize = 1)
	public Long getSubMenuId() {
		return subMenuId;
	}

	public void setSubMenuId(Long subMenuId) {
		this.subMenuId = subMenuId;
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

	@ManyToOne
	@JoinColumn(name = "MENU_ID")
	@JsonIgnore
	public MenuLogin getMenu() {
		return menu;
	}

	public void setMenu(MenuLogin menu) {
		this.menu = menu;
	}

}

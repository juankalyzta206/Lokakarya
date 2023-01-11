package com.ogya.lokakarya.usermanagement.wrapper.login;

import java.util.Set;

import com.ogya.lokakarya.usermanagement.entity.login.SubMenuLogin;


public class MenuLoginWrapper {
	private Long menuId;
	private String nama;
	private String icon;
	private String url;
	private Set<SubMenuLogin> subMenu;
	

	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	//--------------------------------------------------------------------------------------------------------

	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	//--------------------------------------------------------------------------------------------------------




	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	//--------------------------------------------------------------------------------------------------------



	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	//--------------------------------------------------------------------------------------------------------


	

	public Set<SubMenuLogin> getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(Set<SubMenuLogin> subMenu) {
		this.subMenu = subMenu;
	}
	
	//--------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "MenuLoginWrapper [menuId=" + menuId + ", nama=" + nama + ", icon=" + icon + ", url=" + url
				+ ", subMenu=" + subMenu + "]";
	}
	
	
}

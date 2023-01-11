package com.ogya.lokakarya.usermanagement.wrapper.login;

import java.util.Set;

import com.ogya.lokakarya.usermanagement.entity.login.HakAksesLogin;

public class UsersLoginWrapper {
	private String username;
	private String nama;
	private Set<HakAksesLogin> hakAkses;
	

	//--------------------------------------------------------------------------------------------------------

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}	
	//--------------------------------------------------------------------------------------------------------

	public Set<HakAksesLogin> getHakAkses() {
		return hakAkses;
	}
	public void setHakAkses(Set<HakAksesLogin> hakAkses) {
		this.hakAkses = hakAkses;
	}
	//--------------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "UsersLoginWrapper [username=" + username + ", nama=" + nama + ", hakAkses=" + hakAkses + "]";
	}
	
	
	
	
	
	
	
}

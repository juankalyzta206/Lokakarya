package com.ogya.lokakarya.usermanagement.wrapper;

import java.util.Set;

import com.ogya.lokakarya.usermanagement.entity.HakAkses;

public class UsersLoginWrapper {
	private Long userId;
	private String username;
	private String password;
	private String nama;
	private Set<HakAkses> hakAkses;
	
	
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

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	//--------------------------------------------------------------------------------------------------------

	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}	
	//--------------------------------------------------------------------------------------------------------

	public Set<HakAkses> getHakAkses() {
		return hakAkses;
	}
	public void setHakAkses(Set<HakAkses> hakAkses) {
		this.hakAkses = hakAkses;
	}
	//--------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "UsersLoginWrapper [userId=" + userId + ", username=" + username + ", password=" + password + ", nama="
				+ nama + ", hakAkses=" + hakAkses + "]";
	}
	
	
	
	
	
}

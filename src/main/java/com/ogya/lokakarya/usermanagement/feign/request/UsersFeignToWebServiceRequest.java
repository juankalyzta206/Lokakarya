package com.ogya.lokakarya.usermanagement.feign.request;

public class UsersFeignToWebServiceRequest {
	String alamat;
	String email;
	String nama;
	String telpon;
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getTelpon() {
		return telpon;
	}
	public void setTelpon(String telpon) {
		this.telpon = telpon;
	}
	
	
}

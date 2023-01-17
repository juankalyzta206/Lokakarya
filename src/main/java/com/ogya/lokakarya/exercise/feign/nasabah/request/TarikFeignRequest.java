package com.ogya.lokakarya.exercise.feign.nasabah.request;

public class TarikFeignRequest {
	private String noRekening;
	private Long setoran;
	
	public String getNoRekening() {
		return noRekening;
	}
	public void setNoRekening(String noRekening) {
		this.noRekening = noRekening;
	}
	public Long getSetoran() {
		return setoran;
	}
	public void setSetoran(Long setoran) {
		this.setoran = setoran;
	}
	

}

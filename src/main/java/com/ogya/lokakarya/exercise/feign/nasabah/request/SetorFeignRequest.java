package com.ogya.lokakarya.exercise.feign.nasabah.request;

public class SetorFeignRequest {
	private String noRekening;
	private Long tarikan;
	
	public String getNoRekening() {
		return noRekening;
	}
	public void setNoRekening(String noRekening) {
		this.noRekening = noRekening;
	}
	public Long getTarikan() {
		return tarikan;
	}
	public void setTarikan(Long tarikan) {
		this.tarikan = tarikan;
	}
	

}

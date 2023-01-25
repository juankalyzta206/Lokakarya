package com.ogya.lokakarya.exercise.feign.request.transfer;

public class TransferFeignRequest {
	private Long jumlahTranfer;
	private String noRekeningPenerima;
	private String noRekeningPengirim;
	public Long getJumlahTranfer() {
		return jumlahTranfer;
	}
	public void setJumlahTranfer(Long jumlahTranfer) {
		this.jumlahTranfer = jumlahTranfer;
	}
	public String getNoRekeningPenerima() {
		return noRekeningPenerima;
	}
	public void setNoRekeningPenerima(String noRekeningPenerima) {
		this.noRekeningPenerima = noRekeningPenerima;
	}
	public String getNoRekeningPengirim() {
		return noRekeningPengirim;
	}
	public void setNoRekeningPengirim(String noRekeningPengirim) {
		this.noRekeningPengirim = noRekeningPengirim;
	}
	
	
}

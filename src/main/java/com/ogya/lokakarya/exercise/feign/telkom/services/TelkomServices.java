package com.ogya.lokakarya.exercise.feign.telkom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.bankadm.service.TransaksiNasabahService;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.exercise.feign.telkom.repository.TelkomRepository;
import com.ogya.lokakarya.exercise.feign.telkom.request.BayarRequest;
import com.ogya.lokakarya.exercise.feign.telkom.response.BayarResponse;
import com.ogya.lokakarya.exercise.feign.telkom.response.ValidateResponse;


@Service
public class TelkomServices {
	@Autowired
	TelkomRepository telkomRepository;
	@Autowired
	TransaksiNasabahService transaksiNasabahService;
	
	public ValidateResponse callValidateNoTelp (String input) {
		ValidateResponse validateResponse = telkomRepository.validateNoTelp(input);
		return validateResponse;
	}
	public ValidateResponse callValidateNoRek (String input) {
		ValidateResponse validateResponse = telkomRepository.validateNoRek(input);
		return validateResponse;
	}
	
	public BayarResponse callBayarTelkom(BayarRequest request) {
		ValidateResponse validateNotelp = telkomRepository.validateNoTelp(request.getNoTelepon());
		ValidateResponse validateNorek = telkomRepository.validateNoRek(request.getNoRekening());
		if(validateNotelp.getRegistered() && validateNorek.getRegistered()) {
			BayarResponse bayarResponse = telkomRepository.bayarTelkom(request);
			if(bayarResponse.getMessage().equals("Transaksi success")) {
				transaksiNasabahService.bayarTelpon(Long.valueOf(request.getNoRekening()), Long.valueOf(request.getNoTelepon()));
			}
			//bayarResponse.setBulan(request.getBulan());
			return bayarResponse;
		}else {
			throw new BusinessException("Norek/Notelp tidak terdaftar");
		}
	}

}

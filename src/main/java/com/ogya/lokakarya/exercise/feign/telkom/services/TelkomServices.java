package com.ogya.lokakarya.exercise.feign.telkom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.exercise.feign.telkom.repository.TelkomRepository;
import com.ogya.lokakarya.exercise.feign.telkom.request.BayarRequest;
import com.ogya.lokakarya.exercise.feign.telkom.response.BayarResponse;
import com.ogya.lokakarya.exercise.feign.telkom.response.ValidateResponse;


@Service
public class TelkomServices {
	@Autowired
	TelkomRepository telkomRepository;
	
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
			return bayarResponse;
		}else {
			throw new BusinessException("Norek/Notelp tidak terdaftar");
		}
	}

}

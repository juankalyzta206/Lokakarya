package com.ogya.lokakarya.exercise.feign.services.telkom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.repository.telkom.TelkomRepository;
import com.ogya.lokakarya.exercise.feign.request.telkom.BayarRequest;
import com.ogya.lokakarya.exercise.feign.response.telkom.BayarResponse;
import com.ogya.lokakarya.exercise.feign.response.telkom.ValidateResponse;

@Service
public class TelkomFeignServices {
	@Autowired
	TelkomRepository telkomRepository;

	public ValidateResponse callValidateNoTelp(String input) {
		ValidateResponse validateResponse = telkomRepository.validateNoTelp(input);
		return validateResponse;
	}

	public ValidateResponse callValidateNoRek(String input) {
		ValidateResponse validateResponse = telkomRepository.validateNoRek(input);
		return validateResponse;
	}

	public BayarResponse callBayarTelkom(BayarRequest request) {
		BayarResponse bayarResponse = telkomRepository.bayarTelkom(request);
		return bayarResponse;

	}

}

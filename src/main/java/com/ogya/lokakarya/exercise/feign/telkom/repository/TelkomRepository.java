package com.ogya.lokakarya.exercise.feign.telkom.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ogya.lokakarya.exercise.feign.telkom.request.BayarRequest;
import com.ogya.lokakarya.exercise.feign.telkom.response.BayarResponse;
import com.ogya.lokakarya.exercise.feign.telkom.response.ValidateResponse;



@FeignClient(value= "telkom", url = "https://simple-rest-production.up.railway.app/")
public interface TelkomRepository {
	
	@RequestMapping(method = RequestMethod.POST, value = "/telkom/bayar")
	public BayarResponse bayarTelkom(@RequestBody BayarRequest request);
	
	@RequestMapping(method = RequestMethod.GET,value = "/telkom/validate/{no-telpon}")
	public ValidateResponse validateNoTelp(@PathVariable("no-telpon") String input);
	
	@RequestMapping(method = RequestMethod.GET,value = "/nasabah/validate-rekening/{no-rekening}")
	public ValidateResponse validateNoRek(@PathVariable("no-rekening") String input);
}

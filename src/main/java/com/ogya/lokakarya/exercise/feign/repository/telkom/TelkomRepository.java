package com.ogya.lokakarya.exercise.feign.repository.telkom;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ogya.lokakarya.exercise.feign.request.telkom.BayarRequest;
import com.ogya.lokakarya.exercise.feign.response.telkom.BayarResponse;
import com.ogya.lokakarya.exercise.feign.response.telkom.ValidateResponse;



@FeignClient(value= "telkom", url = "https://simple-rest-production.up.railway.app/")
public interface TelkomRepository {
	
	@RequestMapping(method = RequestMethod.POST, value = "/telkom/bayar")
	public BayarResponse bayarTelkom(@RequestBody BayarRequest request);
	
	@RequestMapping(method = RequestMethod.GET,value = "/telkom/validate/{no-telpon}")
	public ValidateResponse validateNoTelp(@PathVariable("no-telpon") String input);
	
	@RequestMapping(method = RequestMethod.GET,value = "/nasabah/validate-rekening/{no-rekening}")
	public ValidateResponse validateNoRek(@PathVariable("no-rekening") String input);
}

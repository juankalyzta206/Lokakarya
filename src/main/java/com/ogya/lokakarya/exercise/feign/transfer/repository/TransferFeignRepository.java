package com.ogya.lokakarya.exercise.feign.transfer.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ogya.lokakarya.exercise.feign.transfer.request.TransferFeignRequest;
import com.ogya.lokakarya.exercise.feign.transfer.response.TransferFeignResponse;
import com.ogya.lokakarya.exercise.feign.transfer.response.ValidateRekeningFeignResponse;

@FeignClient(value= "nasabah", url = "https://simple-rest-production.up.railway.app/")
public interface TransferFeignRepository {
	@RequestMapping(method = RequestMethod.GET, value = "/nasabah/validate-rekening/{no-rekening}")
	public ValidateRekeningFeignResponse validateRekening(@RequestParam("no-rekening") String noRekening);
	
	@RequestMapping(method = RequestMethod.POST, value = "/nasabah/transfer")
	public TransferFeignResponse transfer(@RequestBody TransferFeignRequest request);
}

package com.ogya.lokakarya.exercise.feign.repository.transfer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ogya.lokakarya.exercise.feign.request.transfer.TransferFeignRequest;
import com.ogya.lokakarya.exercise.feign.response.transfer.TransferFeignResponse;
import com.ogya.lokakarya.exercise.feign.response.transfer.ValidateRekeningFeignResponse;

@FeignClient(value= "nasabah", url = "https://simple-rest-production.up.railway.app/")
public interface TransferFeignRepository {
	@RequestMapping(method = RequestMethod.GET, value = "/nasabah/validate-rekening/{no-rekening}")
	public ValidateRekeningFeignResponse validateRekening(@RequestParam("no-rekening") String noRekening);
	
	@RequestMapping(method = RequestMethod.POST, value = "/nasabah/transfer")
	public TransferFeignResponse transfer(@RequestBody TransferFeignRequest request);
}

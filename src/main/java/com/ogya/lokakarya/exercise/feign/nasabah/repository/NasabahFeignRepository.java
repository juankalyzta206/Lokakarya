package com.ogya.lokakarya.exercise.feign.nasabah.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ogya.lokakarya.exercise.feign.nasabah.request.SetorFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.request.TarikFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.response.NasabahFeignResponse;
import com.ogya.lokakarya.exercise.feign.nasabah.response.NoRekeningFeignResponse;

@FeignClient(value= "nasabah2", url = "https://simple-rest-production.up.railway.app/")
public interface NasabahFeignRepository {
	
	@RequestMapping(method = RequestMethod.POST, value = "/nasabah/setor")
	public NasabahFeignResponse setor(@RequestBody SetorFeignRequest request);
	
	@RequestMapping(method = RequestMethod.POST, value = "/nasabah/tarik")
	public NasabahFeignResponse tarik(@RequestBody TarikFeignRequest request); 
	
	@RequestMapping(method = RequestMethod.GET, value = "/nasabah/validate-rekening/{no-rekening}")
	public NoRekeningFeignResponse cekNoRekening(@RequestParam("no-rekening") String noRekening);

}
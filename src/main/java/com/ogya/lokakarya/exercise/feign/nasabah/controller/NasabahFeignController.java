package com.ogya.lokakarya.exercise.feign.nasabah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.exercise.feign.nasabah.request.SetorFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.request.TarikFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.response.NasabahFeignResponse;
import com.ogya.lokakarya.exercise.feign.nasabah.response.NoRekeningFeignResponse;
import com.ogya.lokakarya.exercise.feign.nasabah.services.NasabahFeignService;

@RestController
@RequestMapping(value = "/nasabahWebService")
@CrossOrigin(origins = "*")
public class NasabahFeignController {

	@Autowired
	private NasabahFeignService nasabahFeignService;
	
	@PostMapping(value = "/setor")
	public NasabahFeignResponse callSetor(@RequestBody SetorFeignRequest request) {
		NasabahFeignResponse response = nasabahFeignService.callSetor(request);
		return response;
	}
	
	@PostMapping(value = "/tarik")
	public NasabahFeignResponse callTarik(@RequestBody TarikFeignRequest request) {
		NasabahFeignResponse response = nasabahFeignService.callTarik(request);
		return response;
	}
	
	@GetMapping(value = "/cekNorek/{norek}")
	public NoRekeningFeignResponse cekNoRekening(@RequestParam("norek") String noRekening) {
		NoRekeningFeignResponse response = nasabahFeignService.cekNoRekening(noRekening);
		return response;
	}
}

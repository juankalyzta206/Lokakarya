package com.ogya.lokakarya.exercise.feign.services.nasabah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.repository.nasabah.NasabahFeignRepository;
import com.ogya.lokakarya.exercise.feign.request.nasabah.SetorFeignRequest;
import com.ogya.lokakarya.exercise.feign.request.nasabah.TarikFeignRequest;
import com.ogya.lokakarya.exercise.feign.response.nasabah.NasabahFeignResponse;
import com.ogya.lokakarya.exercise.feign.response.nasabah.NoRekeningFeignResponse;

@Service
public class NasabahFeignService {
	@Autowired
	private NasabahFeignRepository nasabahFeignRepository;
	
	public NasabahFeignResponse callSetor(SetorFeignRequest request) {
		NasabahFeignResponse response = nasabahFeignRepository.setor(request);
		return response;
	}
	
	public NasabahFeignResponse callTarik(TarikFeignRequest request) {
		NasabahFeignResponse response = nasabahFeignRepository.tarik(request);
		return response;
	}
	
	public NoRekeningFeignResponse cekNoRekening(String noRekening) {
		NoRekeningFeignResponse response = nasabahFeignRepository.cekNoRekening(noRekening);
		return response;
	}
}

package com.ogya.lokakarya.exercise.feign.nasabah.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.nasabah.repository.NasabahFeignRepository;
import com.ogya.lokakarya.exercise.feign.nasabah.request.SetorFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.request.TarikFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.response.NasabahFeignResponse;

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
}

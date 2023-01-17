package com.ogya.lokakarya.exercise.feign.laporanpenunggakan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.repository.LaporanPenunggakanRepository;
import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.request.LaporanPenunggakanRequest;
import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.response.LaporanPenunggakanResponse;

@Service
public class LaporanPenunggakanService {
	@Autowired
	LaporanPenunggakanRepository laporanPenunggakanRepository;
	
	public LaporanPenunggakanResponse callLaporanPenunggakan(LaporanPenunggakanRequest request) {
		LaporanPenunggakanResponse laporanPenunggakanResponse = laporanPenunggakanRepository.laporanPenunggakan(request);
		return laporanPenunggakanResponse;
	}
	
	
}

package com.ogya.lokakarya.exercise.feign.laporanpenunggakan.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.request.LaporanPenunggakanRequest;
import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.response.LaporanPenunggakanResponse;

@FeignClient(value= "laporan", url = "https://simple-rest-production.up.railway.app/")
public interface LaporanPenunggakanRepository {
	@RequestMapping(method = RequestMethod.POST, value = "/tunggakan")
	public LaporanPenunggakanResponse laporanPenunggakan(@RequestBody LaporanPenunggakanRequest request);
	
}

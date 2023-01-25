package com.ogya.lokakarya.exercise.feign.controller.laporanpenunggakan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.exercise.feign.request.laporanpenunggakan.LaporanPenunggakanRequest;
import com.ogya.lokakarya.exercise.feign.response.laporanpenunggakan.LaporanPenunggakanResponse;
import com.ogya.lokakarya.exercise.feign.services.laporanpenunggakan.LaporanPenunggakanService;


@RestController
@RequestMapping(value = "/laporan-penunggakan")
@CrossOrigin(origins = "*")
public class LaporanPenunggakanController {
	@Autowired
	LaporanPenunggakanService laporanPenunggakanService;
	@PostMapping(value = "/laporan-exercize")
	public LaporanPenunggakanResponse callLaporan(@RequestBody(required = true) LaporanPenunggakanRequest request) {
		LaporanPenunggakanResponse laporanPenunggakan = laporanPenunggakanService.callLaporanPenunggakan(request);
		return laporanPenunggakan;
	}
}

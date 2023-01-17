package com.ogya.lokakarya.exercise.feign.laporanpenunggakan.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.repository.LaporanPenunggakanRepository;
import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.request.LaporanPenunggakanRequest;
import com.ogya.lokakarya.exercise.feign.laporanpenunggakan.response.LaporanPenunggakanResponse;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;

@Service
public class LaporanPenunggakanService {
	@Autowired
	LaporanPenunggakanRepository laporanPenunggakanRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	
	public LaporanPenunggakanResponse callLaporanPenunggakan(LaporanPenunggakanRequest request) {
		LaporanPenunggakanResponse laporanPenunggakanResponse = laporanPenunggakanRepository.laporanPenunggakan(request);
		if(laporanPenunggakanResponse.getSuccess()) {
			transaksiTelkomRepository.save(toEntity(request));
		}else {
			throw new BusinessException("tidak bisa meng input");
		}
		return laporanPenunggakanResponse;
	}
	private TransaksiTelkom toEntity(LaporanPenunggakanRequest request) {
		TransaksiTelkom entity = new TransaksiTelkom();
		entity.setBulanTagihan(request.getBulanTunggakan().byteValue());
		entity.setTahunTagihan(request.getTahunTagihan());
		entity.setStatus(Byte.valueOf(request.getStatus()));
		entity.setUang(request.getTagihan().longValue());
		MasterPelanggan masterPelanggan = masterPelangganRepository.findByNama(request.getNamaPelanggan());
		entity.setIdPelanggan(masterPelanggan);
		
		return entity;
	}
	
	
}

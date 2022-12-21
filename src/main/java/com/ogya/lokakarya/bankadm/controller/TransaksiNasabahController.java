package com.ogya.lokakarya.bankadm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.bankadm.repository.TransaksiNasabahRepository;
import com.ogya.lokakarya.bankadm.service.TransaksiNasabahService;
import com.ogya.lokakarya.bankadm.util.DataResponse;
import com.ogya.lokakarya.bankadm.util.DataResponseList;
import com.ogya.lokakarya.bankadm.wrapper.TransaksiNasabahWrapper;

@RestController
@RequestMapping(value = "/transfernasabah")
@CrossOrigin()
public class TransaksiNasabahController {
	@Autowired
	TransaksiNasabahRepository transaksiNasabahRepository;
	@Autowired
	TransaksiNasabahService transaksiNasabahService;
	
	@GetMapping(path = "/getByIdPlan")
	public TransaksiNasabahWrapper getByidTransaksiNasabahPlan(@RequestParam("id") Long idTransaksiNasabah) {
		return transaksiNasabahService.getByidTransaksiNasabah(idTransaksiNasabah);
	}
	
	@GetMapping(path = "/getById")
	public DataResponse<TransaksiNasabahWrapper> getByidTransaksiNasabah(@RequestParam("id") Long idTransaksiNasabah) {
		return new DataResponse<TransaksiNasabahWrapper>(transaksiNasabahService.getByidTransaksiNasabah(idTransaksiNasabah));
	}
	
	@GetMapping(path = "/findAllPlan")
	public List<TransaksiNasabahWrapper> findAllPlan() {
		return transaksiNasabahService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<TransaksiNasabahWrapper> findAll() {
		return new DataResponseList<TransaksiNasabahWrapper>(transaksiNasabahService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<TransaksiNasabahWrapper> save(@RequestBody TransaksiNasabahWrapper wrapper){
		return new DataResponse<TransaksiNasabahWrapper>(transaksiNasabahService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<TransaksiNasabahWrapper> update(@RequestBody TransaksiNasabahWrapper wrapper){
		return new DataResponse<TransaksiNasabahWrapper>(transaksiNasabahService.save(wrapper));
	}
	
	@DeleteMapping(path = "/deleteById") 
	public String deleteId(@RequestParam("id")  Long norek) {
		transaksiNasabahService.delete(norek);
		return "Rekening "+norek+" Berhasil di delete";
	}

}

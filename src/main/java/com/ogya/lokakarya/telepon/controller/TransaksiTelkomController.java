package com.ogya.lokakarya.telepon.controller;

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

import com.ogya.lokakarya.telepon.service.TransaksiTelkomService;
import com.ogya.lokakarya.telepon.wrapper.TransaksiTelkomWrapper;
import com.ogya.lokakarya.util.DataResponse;

@RestController
@RequestMapping(value = "/transaksitelkom")
@CrossOrigin()
public class TransaksiTelkomController {
	@Autowired
	TransaksiTelkomService transaksiTelkomService;
	
	@GetMapping(path = "/findAllPlan")
	public List<TransaksiTelkomWrapper> findAllPlan() {
		return transaksiTelkomService.findAll();
	}
	@GetMapping(path = "/findAllSortByMonth")
	public List<TransaksiTelkomWrapper> findAllSortByMonth() {
		return transaksiTelkomService.findAllSortByMonthAndYear();
	}
	@GetMapping(path = "/findStatus1")
	public List<TransaksiTelkomWrapper> findStatus1() {
		return transaksiTelkomService.findAllStatus1();
	}
	@PostMapping(path = "/")
	public DataResponse<TransaksiTelkomWrapper> save(@RequestBody TransaksiTelkomWrapper wrapper) {
		return new DataResponse<TransaksiTelkomWrapper>(transaksiTelkomService.save(wrapper));
	}
	@PutMapping(path = "/")
	public DataResponse<TransaksiTelkomWrapper> update(@RequestBody TransaksiTelkomWrapper wrapper) {
		return new DataResponse<TransaksiTelkomWrapper>(transaksiTelkomService.save(wrapper));
	}
	@DeleteMapping(path = "/deleteById")
	public void delete(@RequestParam("id") Long transaksiTelkomId) {
		transaksiTelkomService.deleteById(transaksiTelkomId);
	}
	@GetMapping(path = "/sumAll")
	public Long sumAll() {
		return transaksiTelkomService.sumAll();
	}

}

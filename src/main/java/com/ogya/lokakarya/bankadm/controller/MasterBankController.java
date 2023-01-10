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

import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.repository.MasterBankRepository;
import com.ogya.lokakarya.bankadm.service.MasterBankService;
import com.ogya.lokakarya.bankadm.wrapper.MasterBankWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;

@RestController
@RequestMapping(value = "/masterbank")
@CrossOrigin(origins = "http://lokakarya-spring-boot-production.up.railway.app")
public class MasterBankController {
	@Autowired
	MasterBankService masterBankService;
	@Autowired
	MasterBankRepository masterBankRepository;
	
	@GetMapping(path = "/getByIdPlan")
	public MasterBankWrapper getByNorekPlan(@RequestParam("id") Long norek) {
		return masterBankService.getByNoRek(norek);
	}
	
	@GetMapping(path = "/getById")
	public DataResponse<MasterBankWrapper> getByNorek(@RequestParam("id") Long norek) {
		return new DataResponse<MasterBankWrapper>(masterBankService.getByNoRek(norek));
	}
	
	@GetMapping(path = "/findAllPlan")
	public List<MasterBankWrapper> findAllPlan() {
		return masterBankService.findAll();
	}
	
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<MasterBankWrapper, MasterBank> findAllWithPagination(@RequestParam("page") 
	int page, @RequestParam("size") int size) {
		return new DataResponsePagination<MasterBankWrapper, MasterBank>(masterBankService.findAllWithPagination(page, size));
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<MasterBankWrapper> findAll() {
		return new DataResponseList<MasterBankWrapper>(masterBankService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<MasterBankWrapper> save(@RequestBody MasterBankWrapper wrapper){
		return new DataResponse<MasterBankWrapper>(masterBankService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<MasterBankWrapper> update(@RequestBody MasterBankWrapper wrapper){
		return new DataResponse<MasterBankWrapper>(masterBankService.save(wrapper));
	}
	
	@DeleteMapping(path = "/deleteById") 
	public void deleteId(@RequestParam("id")  Long norek) {
		masterBankService.delete(norek);
	}
}

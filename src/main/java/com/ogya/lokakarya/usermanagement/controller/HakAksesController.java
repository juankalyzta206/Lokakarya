package com.ogya.lokakarya.usermanagement.controller;

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

import com.ogya.lokakarya.usermanagement.entity.HakAkses;
import com.ogya.lokakarya.usermanagement.service.HakAksesService;
import com.ogya.lokakarya.usermanagement.wrapper.HakAksesWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;


@RestController
@RequestMapping(value = "/hakAkses")
@CrossOrigin(origins = "http://lokakarya-spring-boot-production.up.railway.app")
public class HakAksesController {
	@Autowired
	HakAksesService hakAksesService;
	
	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<HakAksesWrapper, HakAkses> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<HakAksesWrapper, HakAkses>(hakAksesService.findAllWithPagination(page, size));
	}
	
	
	@GetMapping(path = "/findAllPlan")
	public List<HakAksesWrapper> findAllPlan() {
		return hakAksesService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<HakAksesWrapper> findAll() {
		return new DataResponseList<HakAksesWrapper>(hakAksesService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<HakAksesWrapper> save(@RequestBody HakAksesWrapper wrapper){
		return new DataResponse<HakAksesWrapper>(hakAksesService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<HakAksesWrapper> update(@RequestBody HakAksesWrapper wrapper){
		return new DataResponse<HakAksesWrapper>(hakAksesService.save(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long hakAksesId) {
		hakAksesService.delete(hakAksesId);
	}
	
	
}

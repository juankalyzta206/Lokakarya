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

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.service.MasterPelangganService;
import com.ogya.lokakarya.telepon.wrapper.MasterPelangganWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponsePagination;

@RestController
@RequestMapping(value = "/masterpelanggan")
@CrossOrigin(origins = "https://lokakarya-spring-boot-production.up.railway.app")
public class MasterPelangganController {
	@Autowired
	MasterPelangganService masterPelangganService;
	@Autowired
	
	@GetMapping(path = "/findAllPlan")
	public List<MasterPelangganWrapper> findAllPlan() {
		return masterPelangganService.findAll();
	}
	@PostMapping(path = "/")
	public DataResponse<MasterPelangganWrapper> save(@RequestBody MasterPelangganWrapper wrapper) {
		return new DataResponse<MasterPelangganWrapper>(masterPelangganService.save(wrapper));
	}
	@PutMapping(path = "/")
	public DataResponse<MasterPelangganWrapper> update(@RequestBody MasterPelangganWrapper wrapper) {
		return new DataResponse<MasterPelangganWrapper>(masterPelangganService.save(wrapper));
	}
	@DeleteMapping(path = "/deleteById")
	public void delete(@RequestParam("id") Long masterPelangganId) {
		masterPelangganService.deleteById(masterPelangganId);
	}
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.findAllWithPagination(page, size));
	}
}

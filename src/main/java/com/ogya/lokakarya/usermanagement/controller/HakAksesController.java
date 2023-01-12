package com.ogya.lokakarya.usermanagement.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.usermanagement.entity.HakAkses;
import com.ogya.lokakarya.usermanagement.service.HakAksesService;
import com.ogya.lokakarya.usermanagement.wrapper.HakAksesWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;


@RestController
@RequestMapping(value = "/hakAkses")
@CrossOrigin(origins = "*")
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
	
	@RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
    public void exportToPdf(HttpServletResponse response) throws Exception {
		hakAksesService.ExportToPdf(response);
	}
	
	@PostMapping(value = "/findAllWithPaginationAndFilter")
	public DataResponsePagination<HakAksesWrapper, HakAkses> findAllWithPaginationAndFilter(@RequestBody(required = true) PagingRequestWrapper request) {
		return new DataResponsePagination<HakAksesWrapper, HakAkses>(hakAksesService.ListWithPaging(request));
	}
	
	
	
}

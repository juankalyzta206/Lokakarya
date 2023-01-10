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

import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.usermanagement.service.RolesService;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;


@RestController
@RequestMapping(value = "/roles")
@CrossOrigin(origins = "*")
public class RolesController {
	@Autowired
	RolesService rolesService;

	
	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<RolesWrapper, Roles> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<RolesWrapper, Roles>(rolesService.findAllWithPagination(page, size));
	}
			
	
	@GetMapping(path = "/findAllPlan")
	public List<RolesWrapper> findAllPlan() {
		return rolesService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<RolesWrapper> findAll() {
		return new DataResponseList<RolesWrapper>(rolesService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<RolesWrapper> save(@RequestBody RolesWrapper wrapper){
		return new DataResponse<RolesWrapper>(rolesService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<RolesWrapper> update(@RequestBody RolesWrapper wrapper){
		return new DataResponse<RolesWrapper>(rolesService.save(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long roleId) {
		rolesService.delete(roleId);
	}
	
	
}

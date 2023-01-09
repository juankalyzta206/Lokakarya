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

import com.ogya.lokakarya.usermanagement.service.RoleMenuService;
import com.ogya.lokakarya.usermanagement.wrapper.RoleMenuWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;


@RestController
@RequestMapping(value = "/roleMenu")
@CrossOrigin()
public class RoleMenuController {
	@Autowired
	RoleMenuService roleMenuService;
	
	
	@GetMapping(path = "/findAllPlan")
	public List<RoleMenuWrapper> findAllPlan() {
		return roleMenuService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<RoleMenuWrapper> findAll() {
		return new DataResponseList<RoleMenuWrapper>(roleMenuService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<RoleMenuWrapper> save(@RequestBody RoleMenuWrapper wrapper){
		return new DataResponse<RoleMenuWrapper>(roleMenuService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<RoleMenuWrapper> update(@RequestBody RoleMenuWrapper wrapper){
		return new DataResponse<RoleMenuWrapper>(roleMenuService.save(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long roleRoleMenuId) {
		roleMenuService.delete(roleRoleMenuId);
	}
	
	
}

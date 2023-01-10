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

import com.ogya.lokakarya.usermanagement.entity.Menu;
import com.ogya.lokakarya.usermanagement.service.MenuService;
import com.ogya.lokakarya.usermanagement.wrapper.MenuWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;


@RestController
@RequestMapping(value = "/menu")
@CrossOrigin(origins = "https://lokakarya-spring-boot-production.up.railway.app")
public class MenuController {
	@Autowired
	MenuService menuService;
	
	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<MenuWrapper, Menu> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<MenuWrapper, Menu>(menuService.findAllWithPagination(page, size));
	}
	
	@GetMapping(path = "/findAllPlan")
	public List<MenuWrapper> findAllPlan() {
		return menuService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<MenuWrapper> findAll() {
		return new DataResponseList<MenuWrapper>(menuService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<MenuWrapper> save(@RequestBody MenuWrapper wrapper){
		return new DataResponse<MenuWrapper>(menuService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<MenuWrapper> update(@RequestBody MenuWrapper wrapper){
		return new DataResponse<MenuWrapper>(menuService.save(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long menuId) {
		menuService.delete(menuId);
	}
	
	
}

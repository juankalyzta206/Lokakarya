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

import com.ogya.lokakarya.usermanagement.service.SubMenuService;
import com.ogya.lokakarya.usermanagement.wrapper.SubMenuWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;


@RestController
@RequestMapping(value = "/subMenu")
@CrossOrigin()
public class SubMenuController {
	@Autowired
	SubMenuService subMenuService;
	
	
	@GetMapping(path = "/findAllPlan")
	public List<SubMenuWrapper> findAllPlan() {
		return subMenuService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<SubMenuWrapper> findAll() {
		return new DataResponseList<SubMenuWrapper>(subMenuService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<SubMenuWrapper> save(@RequestBody SubMenuWrapper wrapper){
		return new DataResponse<SubMenuWrapper>(subMenuService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<SubMenuWrapper> update(@RequestBody SubMenuWrapper wrapper){
		return new DataResponse<SubMenuWrapper>(subMenuService.save(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long menuId) {
		subMenuService.delete(menuId);
	}
	
	
}

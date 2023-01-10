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

import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.service.UsersService;
import com.ogya.lokakarya.usermanagement.wrapper.UsersLoginWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UpdateUsersWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;


@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "https://lokakarya-spring-boot-production.up.railway.app")
public class UsersController {
	@Autowired
	UsersService userService;
	
	
	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<UsersWrapper, Users> findAllWithPagination(@RequestParam("page") int page,				@RequestParam("size") int size) {
		return new DataResponsePagination<UsersWrapper, Users>(userService.findAllWithPagination(page, size));
	}
	
	@GetMapping(path = "/findAllPlan")
	public List<UsersWrapper> findAllPlan() {
		return userService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<UsersWrapper> findAll() {
		return new DataResponseList<UsersWrapper>(userService.findAll());
	}
	
	@GetMapping(path = "/findById")
	public DataResponseList<UsersWrapper> findById(@RequestParam("id")  Long userId) {
		return new DataResponseList<UsersWrapper>(userService.findByUserId(userId));
	}
	
	@PostMapping(path = "/")
	public DataResponse<UsersWrapper> save(@RequestBody UsersWrapper wrapper){
		return new DataResponse<UsersWrapper>(userService.save(wrapper));
	}
	

	@PutMapping(path = "/")
	public DataResponse<UsersWrapper> update(@RequestBody UpdateUsersWrapper wrapper){
		return new DataResponse<UsersWrapper>(userService.update(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long userId) {
		userService.delete(userId);
	}
	
//	@PostMapping(path = "/register")
//	public DataResponse<LoginWrapper> register(@RequestBody LoginWrapper wrapper){
//		return new DataResponse<LoginWrapper>(loginService.save(wrapper));
//	}
	
	
	@GetMapping(path = "/login")
	public DataResponseList<UsersLoginWrapper> findAll(@RequestParam("identity") String identity, @RequestParam("password") String password) {
		return new DataResponseList<UsersLoginWrapper>(userService.findByEmailOrUsernameAndPassword(identity, password));
	}
	
	
}

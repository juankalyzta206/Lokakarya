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

import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.service.UsersService;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersRegisterWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersUpdateWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.login.UsersLoginWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;


@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "*")
public class UsersController {
	@Autowired
	UsersService userService;
	
	
	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<UsersWrapper, Users> findAllWithPagination(@RequestParam("page") int page,@RequestParam("size") int size) {
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
	public DataResponse<UsersAddWrapper> save(@RequestBody UsersAddWrapper wrapper){
		return new DataResponse<UsersAddWrapper>(userService.save(wrapper));
	}
	

	@PutMapping(path = "/")
	public DataResponse<UsersWrapper> update(@RequestBody UsersUpdateWrapper wrapper){
		return new DataResponse<UsersWrapper>(userService.update(wrapper));
	}
	
	
	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id")  Long userId) {
		userService.delete(userId);
	}
	
	@PostMapping(path = "/register")
	public DataResponse<UsersRegisterWrapper> register(@RequestBody UsersRegisterWrapper wrapper){
		return new DataResponse<UsersRegisterWrapper>(userService.register(wrapper));
	}
	
	
	@PostMapping(path = "/login")
	public DataResponseList<UsersLoginWrapper> findAll(@RequestParam("identity") String identity, @RequestParam("password") String password) {
		return new DataResponseList<UsersLoginWrapper>(userService.findByEmailOrUsernameAndPassword(identity, password));
	}
	
	@RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
	    public void exportToPdf(HttpServletResponse response) throws Exception {
		 userService.ExportToPdf(response);
	}
	
	
	// findAllPagination
	@PostMapping(path = "/findAllWithPaginationAndFilter")
	public DataResponsePagination<UsersWrapper, Users> findAllWithPaginationAndFilter(@RequestBody PagingRequestWrapper wrapper) {
		return new DataResponsePagination<UsersWrapper, Users>(userService.findAllWithPaginationAndFilter(wrapper));
	}
	

}

package com.ogya.lokakarya.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.exercise.feign.usermanagement.request.UsersFeignRequest;
import com.ogya.lokakarya.exercise.feign.usermanagement.services.UsersFeignServices;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;
import com.ogya.lokakarya.util.DataResponseFeign;


@RestController
@RequestMapping(value = "/usersWebService")
@CrossOrigin(origins = "*")
public class UsersWebServiceController {
	@Autowired
	UsersFeignServices usersFeignServices;

	
	@PostMapping(path = "/saveUsersFromWebService")
	public DataResponseFeign<UsersAddWrapper> saveUsersFromWebService(@RequestBody UsersFeignRequest request){
		return usersFeignServices.callUserRoleRecord(request);
	}
	
	@GetMapping(path = "/saveRolesFromWebService/{role}")
	public DataResponseFeign<RolesWrapper> saveRolesFromWebService(@PathVariable String role){
		return usersFeignServices.callUserRoleInquiry(role);
	}
	
	
}

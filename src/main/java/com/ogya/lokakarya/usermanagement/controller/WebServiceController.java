package com.ogya.lokakarya.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.feign.usermanagement.request.UsersFeignRequest;
import com.ogya.lokakarya.feign.usermanagement.services.UsersFeignServices;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;
import com.ogya.lokakarya.util.DataResponse;


@RestController
@RequestMapping(value = "/webService")
@CrossOrigin(origins = "*")
public class WebServiceController {
	@Autowired
	UsersFeignServices usersFeignServices;

	
	@PostMapping(path = "/saveUsersFromWebService")
	public DataResponse<UsersAddWrapper> saveUsersFromWebService(@RequestBody UsersFeignRequest request){
		return new DataResponse<UsersAddWrapper>(usersFeignServices.callUserRoleRecord( request));
	}
	
	@GetMapping(path = "/saveRolesFromWebService/{role}")
	public DataResponse<RolesWrapper> saveRolesFromWebService(@PathVariable String role){
		return new DataResponse<RolesWrapper>(usersFeignServices.callUserRoleInquiry(role));
	}
	
	
}

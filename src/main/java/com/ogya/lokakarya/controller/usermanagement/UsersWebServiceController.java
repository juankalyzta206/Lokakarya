package com.ogya.lokakarya.controller.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.exercise.feign.services.usermanagement.UsersFeignServices;
import com.ogya.lokakarya.util.DataResponseFeign;
import com.ogya.lokakarya.wrapper.usermanagement.RolesWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.UsersAddWrapper;

@RestController
@RequestMapping(value = "/usersWebService")
@CrossOrigin(origins = "*")
public class UsersWebServiceController {
	@Autowired
	UsersFeignServices usersFeignServices;

	@PostMapping(path = "/saveUsersFromWebService")
	public DataResponseFeign<UsersAddWrapper> saveUsersFromWebService(@RequestBody UsersAddWrapper wrapper) {
		return usersFeignServices.callUserRoleRecord(wrapper);
	}

	@PostMapping(path = "/saveRolesFromWebService")
	public DataResponseFeign<RolesWrapper> saveRolesFromWebService(@RequestBody RolesWrapper wrapper) {
		return usersFeignServices.callUserRoleInquiry(wrapper);
	}

}

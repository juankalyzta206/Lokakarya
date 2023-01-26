/*
* UsersFeignServices.java
*	This class is provide service relate to users table 
*	that use another service at web service
*
* Version 1.0
*
* Copyright : Irzan Maulana, Backend Team OGYA
*/
package com.ogya.lokakarya.exercise.feign.services.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.exercise.feign.repository.usermanagement.UsersFeignRepository;
import com.ogya.lokakarya.exercise.feign.request.usermanagement.UsersFeignToWebServiceRequest;
import com.ogya.lokakarya.exercise.feign.response.usermanagement.UsersFeignResponse;
import com.ogya.lokakarya.service.usermanagement.RolesService;
import com.ogya.lokakarya.service.usermanagement.UsersService;
import com.ogya.lokakarya.util.DataResponseFeign;
import com.ogya.lokakarya.wrapper.usermanagement.RolesWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.UsersAddWrapper;

@Service
public class UsersFeignServices {
	@Autowired
	UsersFeignRepository usersFeignRepository;

	@Autowired
	UsersService usersService;

	@Autowired
	RolesService rolesService;

	public DataResponseFeign<RolesWrapper> callUserRoleInquiry(RolesWrapper wrapper) {
		try {
			UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleInquiry(wrapper.getNama());
			if (usersFeignResponse.getSuccess()) {
				wrapper.setProgramName(usersFeignResponse.getProgramName());
				DataResponseFeign<RolesWrapper> dataResponse = new DataResponseFeign<RolesWrapper>(
						rolesService.save(wrapper));
				dataResponse.setSuccess(usersFeignResponse.getSuccess());
				dataResponse.setReferenceNumber(null);
				return dataResponse;
			} else {
				throw new BusinessException("Failed to add Roles, something wrong in web services");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public DataResponseFeign<UsersAddWrapper> callUserRoleRecord(UsersAddWrapper wrapper) {
		try {
			UsersFeignToWebServiceRequest requestWebService = new UsersFeignToWebServiceRequest();
			requestWebService.setAlamat(wrapper.getAlamat());
			requestWebService.setNama(wrapper.getNama());
			requestWebService.setEmail(wrapper.getEmail());
			requestWebService.setTelpon(wrapper.getTelp().toString());
			UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleRecord(requestWebService);
			if (usersFeignResponse.getSuccess()) {
				wrapper.setProgramName(usersFeignResponse.getProgramName());
				DataResponseFeign<UsersAddWrapper> dataResponse = new DataResponseFeign<UsersAddWrapper>(
						usersService.save(wrapper));
				dataResponse.setSuccess(usersFeignResponse.getSuccess());
				dataResponse.setReferenceNumber(null);
				return dataResponse;
			} else {
				throw new BusinessException("Failed to add Users, something wrong in web services");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

	}
}

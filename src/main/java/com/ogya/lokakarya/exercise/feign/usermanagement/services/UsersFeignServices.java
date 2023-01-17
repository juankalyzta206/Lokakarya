package com.ogya.lokakarya.exercise.feign.usermanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.exercise.feign.usermanagement.repository.UsersFeignRepository;
import com.ogya.lokakarya.exercise.feign.usermanagement.request.UsersFeignRequest;
import com.ogya.lokakarya.exercise.feign.usermanagement.request.UsersFeignToWebServiceRequest;
import com.ogya.lokakarya.exercise.feign.usermanagement.response.UsersFeignResponse;
import com.ogya.lokakarya.usermanagement.service.RolesService;
import com.ogya.lokakarya.usermanagement.service.UsersService;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;
import com.ogya.lokakarya.util.DataResponseFeign;

@Service
public class UsersFeignServices {
	@Autowired
	UsersFeignRepository usersFeignRepository;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	RolesService rolesService;

	public DataResponseFeign<RolesWrapper> callUserRoleInquiry(String role) {
		try {
			UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleInquiry(role);
			if (usersFeignResponse.getSuccess()) {
				RolesWrapper addRole = new RolesWrapper();
				addRole.setNama(role);
				addRole.setProgramName(usersFeignResponse.getProgramName());
				
				DataResponseFeign<RolesWrapper> dataResponse = new DataResponseFeign<RolesWrapper>(rolesService.save(addRole));
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
	
	
	public DataResponseFeign<UsersAddWrapper> callUserRoleRecord(UsersFeignRequest request) {
		try {
			UsersFeignToWebServiceRequest requestWebService = new UsersFeignToWebServiceRequest();
			requestWebService.setAlamat(request.getAlamat());
			requestWebService.setNama(request.getNama());
			requestWebService.setEmail(request.getEmail());
			requestWebService.setTelpon(request.getTelpon());
			UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleRecord(requestWebService);
			if (usersFeignResponse.getSuccess()) {
				UsersAddWrapper addUser = new UsersAddWrapper();
				addUser.setUsername(request.getUsername());
				addUser.setPassword(request.getPassword());
				addUser.setAlamat(request.getAlamat());
				addUser.setNama(request.getNama());
				addUser.setTelp(Long.parseLong(request.getTelpon()));
				addUser.setEmail(request.getEmail());
				
				DataResponseFeign<UsersAddWrapper> dataResponse = new DataResponseFeign<UsersAddWrapper>(usersService.save(addUser));
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

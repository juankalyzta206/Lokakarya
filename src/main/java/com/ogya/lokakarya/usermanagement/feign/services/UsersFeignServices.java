package com.ogya.lokakarya.usermanagement.feign.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.feign.repository.UsersFeignRepository;
import com.ogya.lokakarya.usermanagement.feign.request.UsersFeignRequest;
import com.ogya.lokakarya.usermanagement.feign.response.UsersFeignResponse;
import com.ogya.lokakarya.usermanagement.service.RolesService;
import com.ogya.lokakarya.usermanagement.service.UsersService;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;

@Service
public class UsersFeignServices {
	@Autowired
	UsersFeignRepository usersFeignRepository;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	RolesService rolesService;

	public RolesWrapper callUserRoleInquiry(String role) {
		try {
			UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleInquiry(role);
			if (usersFeignResponse.getSuccess()) {
				RolesWrapper addRole = new RolesWrapper();
				addRole.setNama(role);
				addRole.setProgramName(usersFeignResponse.getProgramName());
				return rolesService.save(addRole);
			} else {
				throw new BusinessException("Failed to add Roles, something wrong in web services");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	
	public UsersAddWrapper callUserRoleRecord(String username, String password, UsersFeignRequest request) {
		try {
			UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleRecord(request);
			if (usersFeignResponse.getSuccess()) {
				UsersAddWrapper addUser = new UsersAddWrapper();
				addUser.setUsername(username);
				addUser.setPassword(password);
				addUser.setAlamat(request.getAlamat());
				addUser.setNama(request.getNama());
				addUser.setTelp(Long.parseLong(request.getTelpon()));
				addUser.setEmail(request.getEmail());
				addUser.setProgramName(usersFeignResponse.getProgramName());
				return usersService.save(addUser);
			} else {
				throw new BusinessException("Failed to add Users, something wrong in web services");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
	}
}

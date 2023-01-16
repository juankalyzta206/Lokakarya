package com.ogya.lokakarya.usermanagement.feign.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.usermanagement.feign.repository.UsersFeignRepository;
import com.ogya.lokakarya.usermanagement.feign.request.UsersFeignRequest;
import com.ogya.lokakarya.usermanagement.feign.response.UsersFeignResponse;

@Service
public class UsersFeignServices {
	@Autowired
	UsersFeignRepository usersFeignRepository;
	
	public UsersFeignResponse callUserRoleInquiry(String input) {
		UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleInquiry(input);
		return usersFeignResponse;
	}
	
	public UsersFeignResponse callUserRoleRecord(UsersFeignRequest request) {
		UsersFeignResponse simpleResponse = usersFeignRepository.userRoleRecord(request);
		return simpleResponse;
	}
}

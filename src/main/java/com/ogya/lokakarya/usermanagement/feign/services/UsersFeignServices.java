package com.ogya.lokakarya.usermanagement.feign.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.ogya.lokakarya.usermanagement.feign.repository.UsersFeignRepository;
import com.ogya.lokakarya.usermanagement.feign.request.UsersFeignRequest;
import com.ogya.lokakarya.usermanagement.feign.response.UsersFeignResponse;




public class UsersFeignServices {
	@Autowired
	UsersFeignRepository usersFeignRepository;
	
	public UsersFeignResponse callUserRoleInquiry(String input) {
		UsersFeignResponse usersFeignResponse = usersFeignRepository.userRoleInquiry(input);
		return usersFeignResponse;
	}
	
	public UsersFeignResponse callSimpleGetPost(UsersFeignResponse request) {
		UsersFeignRequest usersFeignRequest = new UsersFeignRequest();
		UsersFeignResponse simpleResponse = usersFeignRepository.userRoleRecord(usersFeignRequest);
		return simpleResponse;
	}
}

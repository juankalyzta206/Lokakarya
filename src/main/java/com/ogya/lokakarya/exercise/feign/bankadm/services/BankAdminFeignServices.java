package com.ogya.lokakarya.exercise.feign.bankadm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.bankadm.repository.BankAdminFeignRepository;
import com.ogya.lokakarya.exercise.feign.bankadm.request.BankAdminFeignRequest;
import com.ogya.lokakarya.exercise.feign.bankadm.response.BankAdminFeignResponse;
@Service
public class BankAdminFeignServices {
	@Autowired
	private BankAdminFeignRepository bankAdminFeignRepository;

	public BankAdminFeignResponse bankPost(BankAdminFeignRequest request) {
		BankAdminFeignResponse response = bankAdminFeignRepository.bankPost(request);
		return response;
	}
}

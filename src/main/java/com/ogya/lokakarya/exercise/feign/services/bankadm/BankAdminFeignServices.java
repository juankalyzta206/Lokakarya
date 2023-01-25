package com.ogya.lokakarya.exercise.feign.services.bankadm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.repository.bankadm.BankAdminFeignRepository;
import com.ogya.lokakarya.exercise.feign.request.bankadm.BankAdminFeignRequest;
import com.ogya.lokakarya.exercise.feign.response.bankadm.BankAdminFeignResponse;
@Service
public class BankAdminFeignServices {
	@Autowired
	private BankAdminFeignRepository bankAdminFeignRepository;

	public BankAdminFeignResponse bankPost(BankAdminFeignRequest request) {
		BankAdminFeignResponse response = bankAdminFeignRepository.bankPost(request);
		return response;
	}
}

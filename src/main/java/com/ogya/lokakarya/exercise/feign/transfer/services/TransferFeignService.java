package com.ogya.lokakarya.exercise.feign.transfer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.transfer.repository.TransferFeignRepository;
import com.ogya.lokakarya.exercise.feign.transfer.request.TransferFeignRequest;
import com.ogya.lokakarya.exercise.feign.transfer.response.TransferFeignResponse;
import com.ogya.lokakarya.exercise.feign.transfer.response.ValidateRekeningFeignResponse;

@Service
public class TransferFeignService {
	@Autowired
	TransferFeignRepository transferRepository;
	
	public ValidateRekeningFeignResponse callValidateRekening(String noRekening) {
		ValidateRekeningFeignResponse validateRekeningResponse = transferRepository.validateRekening(noRekening);
		return validateRekeningResponse;
	}
	
	public TransferFeignResponse callTransfer(TransferFeignRequest request) {
		TransferFeignResponse transferResponse = transferRepository.transfer(request);
		return transferResponse;
	}
}

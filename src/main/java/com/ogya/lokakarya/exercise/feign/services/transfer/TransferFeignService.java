package com.ogya.lokakarya.exercise.feign.services.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exercise.feign.repository.transfer.TransferFeignRepository;
import com.ogya.lokakarya.exercise.feign.request.transfer.TransferFeignRequest;
import com.ogya.lokakarya.exercise.feign.response.transfer.TransferFeignResponse;
import com.ogya.lokakarya.exercise.feign.response.transfer.ValidateRekeningFeignResponse;

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

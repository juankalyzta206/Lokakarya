package com.ogya.lokakarya.exercise.feign.repository.bankadm;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ogya.lokakarya.exercise.feign.request.bankadm.BankAdminFeignRequest;
import com.ogya.lokakarya.exercise.feign.response.bankadm.BankAdminFeignResponse;

@FeignClient(value = "MasterBank", url = "https://simple-rest-production.up.railway.app/")

public interface BankAdminFeignRepository {
	@RequestMapping(method = RequestMethod.POST, value = "/master-data/record")
	public BankAdminFeignResponse bankPost(@RequestBody BankAdminFeignRequest bankAdminReq);
}

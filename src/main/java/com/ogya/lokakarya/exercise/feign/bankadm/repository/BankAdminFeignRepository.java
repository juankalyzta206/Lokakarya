package com.ogya.lokakarya.exercise.feign.bankadm.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ogya.lokakarya.exercise.feign.bankadm.request.BankAdminFeignRequest;
import com.ogya.lokakarya.exercise.feign.bankadm.response.BankAdminFeignResponse;

@FeignClient(value = "MasterBank", url = "https://simple-rest-production.up.railway.app/")

public interface BankAdminFeignRepository {
	@RequestMapping(method = RequestMethod.POST, value = "/simple/post")
	public BankAdminFeignResponse bankPost(@RequestBody BankAdminFeignRequest bankAdminReq);
}

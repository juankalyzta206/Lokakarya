package com.ogya.lokakarya.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Login;
import com.ogya.lokakarya.usermanagement.repository.LoginRepository;
import com.ogya.lokakarya.usermanagement.wrapper.LoginWrapper;

@Service
@Transactional
public class LoginService {
	@Autowired
	LoginRepository loginRepository;
	
	public List<LoginWrapper> findByEmailOrUsernameAndPassword(String identity, String password) {
		if (loginRepository.isRegisteredEmail(identity) == 0) {
			if (loginRepository.isRegisteredUsername(identity) == 0) {
				throw new BusinessException("Email or Username is not Registered");
			} else {
				if (loginRepository.isMatchUsername(identity, password) == 0) {
					throw new BusinessException("Wrong Password");
				} else {
					List<Login> loginList = loginRepository.findByUsernameAndPassword(identity, password);
					return toWrapperList(loginList);
				}
			}
		} else {
			if (loginRepository.isMatchEmail(identity, password) == 0) {
				throw new BusinessException("Wrong Password");
			} else {
				List<Login> loginList = loginRepository.findByEmailAndPassword(identity, password);
				return toWrapperList(loginList);
			}
		}
	}
	
	

	private LoginWrapper toWrapper(Login entity) {
		LoginWrapper wrapper = new LoginWrapper();
		wrapper.setHakAkses(entity.getHakAkses());
		wrapper.setUserId(entity.getUserId());
		wrapper.setUsername(entity.getUsername());
		wrapper.setPassword(entity.getPassword());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setEmail(entity.getEmail());
		wrapper.setTelp(entity.getTelp());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}
	

	private List<LoginWrapper> toWrapperList(List<Login> entityList) {
		List<LoginWrapper> wrapperList = new ArrayList<LoginWrapper>();
		for (Login entity : entityList) {
			LoginWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public List<LoginWrapper> findAll() {
		List<Login> userList = loginRepository.findAll(Sort.by(Order.by("userId")).ascending());
		return toWrapperList(userList);
	}

	private Login toEntity(LoginWrapper wrapper) {
		Login entity = new Login();
		if (wrapper.getUserId() != null) {
			entity = loginRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setHakAkses(wrapper.getHakAkses());
		entity.setUsername(wrapper.getUsername());
		entity.setPassword(wrapper.getPassword());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setEmail(wrapper.getEmail());
		entity.setTelp(wrapper.getTelp());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public LoginWrapper save(LoginWrapper wrapper) {
		if (loginRepository.isRegisteredEmail(wrapper.getEmail()) == 0) {
			if (loginRepository.isRegisteredUsername(wrapper.getUsername()) == 0) {
				Login user = loginRepository.save(toEntity(wrapper));
				return toWrapper(user);
			} else {
				throw new BusinessException("Username already taken");
			}
		} else {
			throw new BusinessException("Email already taken");
		}
	}

}
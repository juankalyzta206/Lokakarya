package com.ogya.lokakarya.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.usermanagement.repository.RolesRepository;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;

@Service
@Transactional
public class RolesService {
	@Autowired
	RolesRepository rolesRepository;
	

	private RolesWrapper toWrapper(Roles entity) {
		RolesWrapper wrapper = new RolesWrapper();
		wrapper.setRoleMenu(entity.getRoleMenu());
		wrapper.setRoleId(entity.getRoleId());
		wrapper.setNama(entity.getNama());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}

	private List<RolesWrapper> toWrapperList(List<Roles> entityList) {
		List<RolesWrapper> wrapperList = new ArrayList<RolesWrapper>();
		for (Roles entity : entityList) {
			RolesWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<RolesWrapper> findAll() {
		List<Roles> rolesList = rolesRepository.findAll(Sort.by(Order.by("roleId")).ascending());
		return toWrapperList(rolesList);
	}

	private Roles toEntity(RolesWrapper wrapper) {
		Roles entity = new Roles();
		if (wrapper.getRoleId() != null) {
			entity = rolesRepository.getReferenceById(wrapper.getRoleId());
		}
		entity.setNama(wrapper.getNama());
		entity.setRoleMenu(wrapper.getRoleMenu());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public RolesWrapper save(RolesWrapper wrapper) {
		Roles roles = rolesRepository.save(toEntity(wrapper));
		return toWrapper(roles);
	}
	
	public void delete(Long id) {
		if (rolesRepository.isExistHakAkses(id) == 0) {
			if (rolesRepository.isExistRoleMenu(id) == 0) {
				rolesRepository.deleteById(id);
			} else {
				throw new BusinessException("Role ID cannot deleted. Role ID is still used in the ROLE_MENU table");
			}
		} else {
			throw new BusinessException("Role ID cannot deleted. Role ID is still used in the HAK_AKSES table");
		}
		
	}
}
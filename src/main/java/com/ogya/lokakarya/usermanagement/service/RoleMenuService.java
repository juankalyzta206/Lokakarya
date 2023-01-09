package com.ogya.lokakarya.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Menu;
import com.ogya.lokakarya.usermanagement.entity.RoleMenu;
import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.usermanagement.repository.MenuRepository;
import com.ogya.lokakarya.usermanagement.repository.RoleMenuRepository;
import com.ogya.lokakarya.usermanagement.repository.RolesRepository;
import com.ogya.lokakarya.usermanagement.wrapper.RoleMenuWrapper;

@Service
@Transactional
public class RoleMenuService {
	@Autowired
	RoleMenuRepository roleMenuRepository;
	
	@Autowired
	MenuRepository menuRepository;
	
	@Autowired
	RolesRepository rolesRepository;

	private RoleMenuWrapper toWrapper(RoleMenu entity) {
		RoleMenuWrapper wrapper = new RoleMenuWrapper();
		wrapper.setRoleMenuId(entity.getRoleMenuId());
		wrapper.setRoleId(entity.getRoles() != null ? entity.getRoles().getRoleId() : null);
		wrapper.setMenuId(entity.getMenu() != null ? entity.getMenu().getMenuId() : null);
		wrapper.setRoleName(entity.getRoles() != null ? entity.getRoles().getNama() : null);
		wrapper.setMenuName(entity.getMenu() != null ? entity.getMenu().getNama() : null);
		wrapper.setIsActive(entity.getIsActive());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}

	private List<RoleMenuWrapper> toWrapperList(List<RoleMenu> entityList) {
		List<RoleMenuWrapper> wrapperList = new ArrayList<RoleMenuWrapper>();
		for (RoleMenu entity : entityList) {
			RoleMenuWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<RoleMenuWrapper> findAll() {
		List<RoleMenu> roleMenuList = roleMenuRepository.findAll(Sort.by(Order.by("roleMenuId")).ascending());
		return toWrapperList(roleMenuList);
	}

	private RoleMenu toEntity(RoleMenuWrapper wrapper) {
		RoleMenu entity = new RoleMenu();
		if (wrapper.getRoleMenuId() != null) {
			entity = roleMenuRepository.getReferenceById(wrapper.getRoleMenuId());
		}
		Optional<Roles> optionalRoles = rolesRepository.findById(wrapper.getRoleId());
		Roles roles = optionalRoles.isPresent() ? optionalRoles.get() : null;
		entity.setRoles(roles);
		Optional<Menu> optionalMenu = menuRepository.findById(wrapper.getMenuId());
		Menu menu = optionalMenu.isPresent() ? optionalMenu.get() : null;
		entity.setMenu(menu);
		entity.setIsActive(wrapper.getIsActive());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public RoleMenuWrapper save(RoleMenuWrapper wrapper) {
		if (roleMenuRepository.isExistRoleMenu(wrapper.getRoleId(),wrapper.getMenuId()) > 0) {
			throw new BusinessException("Cannot add role menu, same Role ID and Menu ID already exist");
		}
		if (roleMenuRepository.isExistRole(wrapper.getRoleId()) == 0) {
			throw new BusinessException("Cannot add role menu, Role ID not exist");
		}
		if (roleMenuRepository.isExistMenu(wrapper.getMenuId()) == 0) {
			throw new BusinessException("Cannot add role menu, Menu ID not exist");
		}
		RoleMenu roleMenu = roleMenuRepository.save(toEntity(wrapper));
		return toWrapper(roleMenu);
	}

	public void delete(Long id) {
		roleMenuRepository.deleteById(id);
	}
}
package com.ogya.lokakarya.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Menu;
import com.ogya.lokakarya.usermanagement.repository.MenuRepository;
import com.ogya.lokakarya.usermanagement.wrapper.MenuWrapper;

@Service
@Transactional
public class MenuService {
	@Autowired
	MenuRepository menuRepository;
	

	private MenuWrapper toWrapper(Menu entity) {
		MenuWrapper wrapper = new MenuWrapper();
		wrapper.setSubMenu(entity.getSubMenu());
		wrapper.setMenuId(entity.getMenuId());
		wrapper.setNama(entity.getNama());
		wrapper.setIcon(entity.getIcon());
		wrapper.setUrl(entity.getUrl());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}

	private List<MenuWrapper> toWrapperList(List<Menu> entityList) {
		List<MenuWrapper> wrapperList = new ArrayList<MenuWrapper>();
		for (Menu entity : entityList) {
			MenuWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<MenuWrapper> findAll() {
		List<Menu> menulist = menuRepository.findAll(Sort.by(Order.by("menuId")).ascending());
		return toWrapperList(menulist);
	}

	private Menu toEntity(MenuWrapper wrapper) {
		Menu entity = new Menu();
		if (wrapper.getMenuId() != null) {
			entity = menuRepository.getReferenceById(wrapper.getMenuId());
		}
		entity.setSubMenu(wrapper.getSubMenu());
		entity.setNama(wrapper.getNama());
		entity.setIcon(wrapper.getIcon());
		entity.setUrl(wrapper.getUrl());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public MenuWrapper save(MenuWrapper wrapper) {
		Menu menu = menuRepository.save(toEntity(wrapper));
		return toWrapper(menu);
	}

	public void delete(Long id) {
		if (menuRepository.isExistRoleMenu(id) == 0) {
			menuRepository.deleteById(id);
		} else {
			throw new BusinessException("Menu ID cannot deleted. Menu ID is still used in the ROLE_MENU table");
		}
		
	}
}
package com.ogya.lokakarya.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>{
	@Query(value="SELECT COUNT(*) FROM ROLE_MENU rm WHERE rm.MENU_ID = :menuId", 
			nativeQuery = true)
	Long isExistRoleMenu(@Param("menuId") Long menuId);
}
package com.ogya.lokakarya.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.RoleMenu;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
	@Query(value = "SELECT COUNT(*) FROM MENU m WHERE m.MENU_ID = :menuId", nativeQuery = true)
	Long isExistMenu(@Param("menuId") Long menuId);

	@Query(value = "SELECT COUNT(*) FROM ROLES r WHERE r.ROLE_ID = :roleId", nativeQuery = true)
	Long isExistRole(@Param("roleId") Long roleId);

	@Query(value = "SELECT COUNT(*) FROM ROLE_MENU rm WHERE rm.ROLE_ID= :roleId AND rm.MENU_ID = :menuId", nativeQuery = true)
	Long isExistRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}
package com.ogya.lokakarya.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.entity.usermanagement.SubMenu;

public interface SubMenuRepository extends JpaRepository<SubMenu, Long> {
	@Query(value = "SELECT COUNT(*) FROM MENU m WHERE m.MENU_ID = :menuId", nativeQuery = true)
	Long isExistMenu(@Param("menuId") Long menuId);
}
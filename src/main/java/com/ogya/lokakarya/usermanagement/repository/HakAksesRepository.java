package com.ogya.lokakarya.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.HakAkses;

public interface HakAksesRepository extends JpaRepository<HakAkses, Long>{
	@Query(value="SELECT COUNT(*) FROM USERS u WHERE u.USER_ID = :userId", 
			nativeQuery = true)
	Long isExistUser(@Param("userId") Long userId);
	
	@Query(value="SELECT COUNT(*) FROM ROLES r WHERE r.ROLE_ID = :roleId", 
			nativeQuery = true)
	Long isExistRole(@Param("roleId") Long roleId);
	
	@Query(value="SELECT COUNT(*) FROM HAK_AKSES ha WHERE ha.USER_ID = :userId AND ha.ROLE_ID = :roleId", 
			nativeQuery = true)
	Long isExistHakAkses(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
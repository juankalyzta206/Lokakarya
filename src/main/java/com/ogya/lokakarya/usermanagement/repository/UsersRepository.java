package com.ogya.lokakarya.usermanagement.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.Users;


public interface UsersRepository extends JpaRepository<Users, Long>{
	List<Users> findByEmailAndPassword (String email, String password);
	List<Users> findByUserId (Long userId);
	
	@Query(value="SELECT COUNT(1) FROM USERS u WHERE u.USERNAME = :username", 
			nativeQuery = true)
	Long checkUsername(@Param("username") String username);
	
	@Query(value="SELECT COUNT(1) FROM USERS u WHERE u.EMAIL = :email", 
			nativeQuery = true)
	Long checkEmail(@Param("email") String email);
	
	@Query(value="SELECT COUNT(*) FROM HAK_AKSES ha WHERE ha.USER_ID = :userId", 
			nativeQuery = true)
	Long isExistHakAkses(@Param("userId") Long userId);
}
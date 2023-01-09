package com.ogya.lokakarya.usermanagement.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.Login;


public interface LoginRepository extends JpaRepository<Login, Long>{
	List<Login> findByEmailAndPassword (String email, String password);
	List<Login> findByUsernameAndPassword (String username, String password);
	
	@Query(value="SELECT COUNT(*) FROM USERS u WHERE u.EMAIL = :email", 
			nativeQuery = true)
	Long isRegisteredEmail(@Param("email") String email);
	
	@Query(value="SELECT COUNT(*) FROM USERS u WHERE u.USERNAME = :username", 
			nativeQuery = true)
	Long isRegisteredUsername(@Param("username") String username);
	
	@Query(value="SELECT COUNT(*) FROM USERS u WHERE u.EMAIL = :email AND u.password = :password", 
			nativeQuery = true)
	Long isMatchEmail(@Param("email") String email, @Param("password") String password);
	
	@Query(value="SELECT COUNT(*) FROM USERS u WHERE u.USERNAME = :username AND u.password = :password", 
			nativeQuery = true)
	Long isMatchUsername(@Param("username") String username, @Param("password") String password);
}
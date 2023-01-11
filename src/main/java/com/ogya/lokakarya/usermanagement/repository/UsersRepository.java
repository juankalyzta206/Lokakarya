package com.ogya.lokakarya.usermanagement.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.util.FilterWrapper;


public interface UsersRepository extends JpaRepository<Users, Long>{
	List<Users> findByEmailAndPassword (String email, String password);
	List<Users> findByUserId (Long userId);
	List<Users> findByUsernameAndPassword (String username, String password);
	
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

	@Query(value="SELECT PASSWORD FROM USERS u WHERE u.USERNAME = :username", 
			nativeQuery = true)
	String hashedPasswordUsername(@Param("username") String username);
	
	@Query(value="SELECT PASSWORD FROM USERS u WHERE u.EMAIL = :email", 
			nativeQuery = true)
	String hashedPasswordEmail(@Param("email") String email);

	
	@Query(value="SELECT COUNT(1) FROM USERS u WHERE u.USERNAME = :username", 
			nativeQuery = true)
	Long checkUsername(@Param("username") String username);
	
	@Query(value="SELECT COUNT(1) FROM USERS u WHERE u.EMAIL = :email", 
			nativeQuery = true)
	Long checkEmail(@Param("email") String email);
	
	@Query(value="SELECT COUNT(*) FROM HAK_AKSES ha WHERE ha.USER_ID = :userId", 
			nativeQuery = true)
	Long isExistHakAkses(@Param("userId") Long userId);
	
	
	@Query(value = "SELECT * FROM USERS u "
			+ "WHERE LOWER(u.USERNAME) LIKE LOWER(CONCAT(CONCAT('%',:username),'%')) "
			+ "AND LOWER(u.NAMA) LIKE LOWER(CONCAT(CONCAT('%',:nama),'%')) "
			+ "AND LOWER(u.EMAIL) LIKE LOWER(CONCAT(CONCAT('%',:email),'%')) "
			,
	           countQuery = "SELECT COUNT(*) FROM USERS u "
	           		+ "WHERE LOWER(u.USERNAME) LIKE LOWER(CONCAT(CONCAT('%',:username),'%')) "
	           		+ "AND LOWER(u.NAMA) LIKE LOWER(CONCAT(CONCAT('%',:nama),'%')) "
	    			+ "AND LOWER(u.EMAIL) LIKE LOWER(CONCAT(CONCAT('%',:email),'%')) "
	    			,
	           nativeQuery = true)
	Page<Users> findAllWithPaginationAndFilter(@Param("username") String username,  
			@Param("nama") String nama,
			@Param("email") String email,
//			@Param("sortField") String sortField,
			Pageable paging);
	

	
}
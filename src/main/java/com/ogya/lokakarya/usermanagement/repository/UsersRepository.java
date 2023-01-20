package com.ogya.lokakarya.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	List<Users> findByEmailAndPassword(String email, String password);

	List<Users> findByUserId(Long userId);

	List<Users> findByUsernameAndPassword(String username, String password);

	@Query(value = "SELECT * " + "FROM   USERS " + "WHERE  CREATED_DATE < TRUNC( SYSDATE ) "
			+ "AND  CREATED_DATE > TRUNC( SYSDATE ) - INTERVAL '1' DAY " + "ORDER BY CREATED_DATE", nativeQuery = true)
	List<Users> newUsersDaily();

	@Query(value = "SELECT * " + "FROM   USERS " + "WHERE  CREATED_DATE < TRUNC( SYSDATE ) "
			+ "AND  CREATED_DATE > TRUNC( SYSDATE ) - INTERVAL '1' MONTH "
			+ "ORDER BY CREATED_DATE", nativeQuery = true)
	List<Users> newUsersMonthly();

	@Query(value = "SELECT * " + "FROM   USERS " + "WHERE  CREATED_DATE < TRUNC( SYSDATE ) "
			+ "AND  CREATED_DATE > TRUNC( SYSDATE ) - INTERVAL '7' DAY " + "ORDER BY CREATED_DATE", nativeQuery = true)
	List<Users> newUsersWeekly();

	@Query(value = "SELECT COUNT(*) FROM USERS u WHERE u.EMAIL = :email", nativeQuery = true)
	Long isRegisteredEmail(@Param("email") String email);

	@Query(value = "SELECT COUNT(*) FROM USERS u WHERE u.USERNAME = :username", nativeQuery = true)
	Long isRegisteredUsername(@Param("username") String username);

	@Query(value = "SELECT COUNT(*) FROM USERS u WHERE u.EMAIL = :email AND u.password = :password", nativeQuery = true)
	Long isMatchEmail(@Param("email") String email, @Param("password") String password);

	@Query(value = "SELECT COUNT(*) FROM USERS u WHERE u.USERNAME = :username AND u.password = :password", nativeQuery = true)
	Long isMatchUsername(@Param("username") String username, @Param("password") String password);

	@Query(value = "SELECT PASSWORD FROM USERS u WHERE u.USERNAME = :username", nativeQuery = true)
	String hashedPasswordUsername(@Param("username") String username);

	@Query(value = "SELECT PASSWORD FROM USERS u WHERE u.EMAIL = :email", nativeQuery = true)
	String hashedPasswordEmail(@Param("email") String email);

	@Query(value = "SELECT COUNT(1) FROM USERS u WHERE u.USERNAME = :username", nativeQuery = true)
	Long checkUsername(@Param("username") String username);

	@Query(value = "SELECT COUNT(1) FROM USERS u WHERE u.EMAIL = :email", nativeQuery = true)
	Long checkEmail(@Param("email") String email);

	@Query(value = "SELECT COUNT(*) FROM HAK_AKSES ha WHERE ha.USER_ID = :userId", nativeQuery = true)
	Long isExistHakAkses(@Param("userId") Long userId);

}
package com.ogya.lokakarya.usermanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.usermanagement.entity.Users;


public interface UsersRepository extends JpaRepository<Users, Long>{
	
	List<Users> findByEmailAndPassword (String email, String password);
	List<Users> findByUserId (Long userId);
	List<Users> findByUsernameAndPassword (String username, String password);
	
	@Query(value="SELECT * "
			+ "FROM   USERS "
			+ "WHERE  CREATED_DATE < TRUNC( SYSDATE ) "
			+ "AND  CREATED_DATE > TRUNC( SYSDATE ) - INTERVAL '1' DAY "
			+ "ORDER BY CREATED_DATE", 
			nativeQuery = true)
	List<Users> newUsersDaily ();
	
	@Query(value="SELECT * "
			+ "FROM   USERS "
			+ "WHERE  CREATED_DATE < TRUNC( SYSDATE ) "
			+ "AND  CREATED_DATE > TRUNC( SYSDATE ) - INTERVAL '1' MONTH "
			+ "ORDER BY CREATED_DATE", 
			nativeQuery = true)
	List<Users> newUsersMonthly ();
	
	@Query(value="SELECT * "
			+ "FROM   USERS "
			+ "WHERE  CREATED_DATE < TRUNC( SYSDATE ) "
			+ "AND  CREATED_DATE > TRUNC( SYSDATE ) - INTERVAL '7' DAY "
			+ "ORDER BY CREATED_DATE", 
			nativeQuery = true)
	List<Users> newUsersWeekly ();
	
	
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
	
	
	
	
	@Query(value="SELECT * FROM USERS WHERE "
			+ "(LOWER(USER_ID) LIKE LOWER(CONCAT(CONCAT('%',:pUserId),'%'))) AND "
			+ "(LOWER(USERNAME) LIKE LOWER(CONCAT(CONCAT('%',:pUsername),'%'))) AND "
			+ "(LOWER(EMAIL) LIKE LOWER(CONCAT(CONCAT('%',:pEmail),'%'))) AND "
			+ "(LOWER(NAMA) LIKE LOWER(CONCAT(CONCAT('%',:pNama),'%')) OR NAMA IS NULL) AND "
			+ "(LOWER(TELP) LIKE LOWER(CONCAT(CONCAT('%',:pTelp),'%')) OR TELP IS NULL) AND "
			+ "(LOWER(PROGRAM_NAME) LIKE LOWER(CONCAT(CONCAT('%',:pProgramName),'%')) OR PROGRAM_NAME IS NULL) AND "
			+ "(LOWER(ALAMAT) LIKE LOWER(CONCAT(CONCAT('%',:pAlamat),'%')) OR ALAMAT IS NULL) AND "
			+ "(LOWER(CREATED_DATE) LIKE LOWER(CONCAT(CONCAT('%',:pCreatedDate),'%')) OR CREATED_DATE IS NULL) AND "
			+ "(LOWER(CREATED_BY) LIKE LOWER(CONCAT(CONCAT('%',:pCreatedBy),'%')) OR CREATED_BY IS NULL) AND "
			+ "(LOWER(UPDATED_DATE) LIKE LOWER(CONCAT(CONCAT('%',:pUpdatedDate),'%')) OR UPDATED_DATE IS NULL) AND "
			+ "(LOWER(UPDATED_BY) LIKE LOWER(CONCAT(CONCAT('%',:pUpdatedBy),'%')) OR UPDATED_BY IS NULL)",
			countQuery = "SELECT COUNT(*) FROM USERS WHERE "
					+ "(LOWER(USER_ID) LIKE LOWER(CONCAT(CONCAT('%',:pUserId),'%'))) AND "
					+ "(LOWER(USERNAME) LIKE LOWER(CONCAT(CONCAT('%',:pUsername),'%'))) AND "
					+ "(LOWER(EMAIL) LIKE LOWER(CONCAT(CONCAT('%',:pEmail),'%'))) AND "
					+ "(LOWER(NAMA) LIKE LOWER(CONCAT(CONCAT('%',:pNama),'%')) OR NAMA IS NULL) AND "
					+ "(LOWER(TELP) LIKE LOWER(CONCAT(CONCAT('%',:pTelp),'%')) OR TELP IS NULL) AND "
					+ "(LOWER(PROGRAM_NAME) LIKE LOWER(CONCAT(CONCAT('%',:pProgramName),'%')) OR PROGRAM_NAME IS NULL) AND "
					+ "(LOWER(ALAMAT) LIKE LOWER(CONCAT(CONCAT('%',:pAlamat),'%')) OR ALAMAT IS NULL) AND "
					+ "(LOWER(CREATED_DATE) LIKE LOWER(CONCAT(CONCAT('%',:pCreatedDate),'%')) OR CREATED_DATE IS NULL) AND "
					+ "(LOWER(CREATED_BY) LIKE LOWER(CONCAT(CONCAT('%',:pCreatedBy),'%')) OR CREATED_BY IS NULL) AND "
					+ "(LOWER(UPDATED_DATE) LIKE LOWER(CONCAT(CONCAT('%',:pUpdatedDate),'%')) OR UPDATED_DATE IS NULL) AND "
					+ "(LOWER(UPDATED_BY) LIKE LOWER(CONCAT(CONCAT('%',:pUpdatedBy),'%')) OR UPDATED_BY IS NULL)",	
			nativeQuery = true)
	Page<Users> filterQuery(
			@Param("pUserId") String userId, 
			@Param("pUsername") String username, 
			@Param("pNama") String nama, 
			@Param("pAlamat") String alamat, 
			@Param("pEmail") String email, 
			@Param("pTelp") String telp, 
			@Param("pProgramName") String programName, 
			@Param("pCreatedDate") String createdDate, 
			@Param("pCreatedBy") String createdBy,
			@Param("pUpdatedDate") String updatedDate,
			@Param("pUpdatedBy") String updatedBy,
			Pageable paging);

	
}
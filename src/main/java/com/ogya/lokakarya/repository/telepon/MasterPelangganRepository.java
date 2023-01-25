package com.ogya.lokakarya.repository.telepon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.entity.telepon.MasterPelanggan;
import com.ogya.lokakarya.entity.usermanagement.Users;


public interface MasterPelangganRepository extends JpaRepository<MasterPelanggan, Long> {
	MasterPelanggan findByNoTelp (Long noTelpon);
	MasterPelanggan findByIdPelanggan (Long idPelanggan);
	Page<MasterPelanggan> findAll(Pageable page);
	
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE e.idPelanggan = :pidPelanggan ")
	<T>Page<MasterPelanggan> findAll(Pageable page,@Param("pidPelanggan") T pidPelanggan);
	
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE :filter = :value ")
	<T>Page<MasterPelanggan> findAll(Pageable page,@Param("filter") String filter,@Param("value") T value);
	
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE e.nama = :nama ")
	<T>Page<MasterPelanggan> findAllName(Pageable page,@Param("nama") T nama);
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE e.alamat = :alamat ")
	<T>Page<MasterPelanggan> findAllAlamat(Pageable page,@Param("alamat") T alamat);
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE e.noTelp = :noTelp ")
	<T>Page<MasterPelanggan> findAllNotelp(Pageable page,@Param("noTelp") T noTelp);
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE e.idPelanggan = :pidPelanggan ")
	<T>Page<MasterPelanggan> findAllIdPelanggan(Pageable page,@Param("pidPelanggan") T pidPelanggan);
	@Query(value = "SELECT * FROM MASTER_PELANGGAN e WHERE USER_ID = :userId" , nativeQuery=true)
	<T>Page<MasterPelanggan> findAllUserId(Pageable page,@Param("userId") T userId);
	
	//List<MasterPelanggan> findByidPelangganOrNamaIgnoreCaseContainingOrAlamatIgnoreCaseContainingOrNoTelpOrUsers(Long idPelanggan,String nama,String alamat,Long noTelp,Users user);
	Page<MasterPelanggan> findByidPelangganOrNamaIgnoreCaseContainingOrAlamatIgnoreCaseContainingOrNoTelpOrUsers(Pageable page,Long idPelanggan,String nama,String alamat,Long noTelp,Users user);
	
	MasterPelanggan findByNama(String nama);
}

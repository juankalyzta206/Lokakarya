package com.ogya.lokakarya.telepon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;


public interface MasterPelangganRepository extends JpaRepository<MasterPelanggan, Long> {
	MasterPelanggan findByNoTelp (Long noTelpon);
	
	Page<MasterPelanggan> findAll(Pageable page);
	
	@Query(value = "SELECT e FROM MasterPelanggan e WHERE e.idPelanggan = :pidPelanggan ")
	Page<MasterPelanggan> findAll(Pageable page,@Param("pidPelanggan") Long pidPelanggan);
}

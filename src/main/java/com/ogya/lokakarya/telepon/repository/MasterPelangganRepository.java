package com.ogya.lokakarya.telepon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;

public interface MasterPelangganRepository extends JpaRepository<MasterPelanggan, Long> {
	MasterPelanggan findByNoTelp (Long noTelpon);
}

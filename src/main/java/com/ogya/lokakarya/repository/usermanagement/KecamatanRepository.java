package com.ogya.lokakarya.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.entity.usermanagement.alamat.Kecamatan;

public interface KecamatanRepository extends JpaRepository<Kecamatan, Long> {

}
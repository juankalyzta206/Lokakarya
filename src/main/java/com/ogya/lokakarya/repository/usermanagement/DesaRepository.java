package com.ogya.lokakarya.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.entity.usermanagement.alamat.Desa;

public interface DesaRepository extends JpaRepository<Desa, Long> {

}
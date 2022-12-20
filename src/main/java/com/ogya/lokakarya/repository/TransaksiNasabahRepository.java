package com.ogya.lokakarya.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.entity.TransaksiNasabah;

public interface TransaksiNasabahRepository extends JpaRepository<TransaksiNasabah, Long> {
	

	List<TransaksiNasabah> findByidTransaksiNasabah(Long idTransaksiNasabah);
	Page<TransaksiNasabah> findAll(Pageable page);

}

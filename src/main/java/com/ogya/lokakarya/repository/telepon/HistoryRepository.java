package com.ogya.lokakarya.repository.telepon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ogya.lokakarya.entity.telepon.HistoryTelkom;

public interface HistoryRepository extends JpaRepository<HistoryTelkom, Long> {
	@Query(value = "SELECT SUM(UANG) FROM HISTORY_TELKOM", nativeQuery = true)
	Long sumAll();
	Page<HistoryTelkom> findAll(Pageable page);
}

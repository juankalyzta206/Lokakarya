package com.ogya.lokakarya.telepon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ogya.lokakarya.telepon.entity.HistoryTelkom;

public interface HistoryRepository extends JpaRepository<HistoryTelkom, Long> {
	@Query(value = "SELECT SUM(UANG) FROM HISTORY_TELKOM", nativeQuery = true)
	Long sumAll();
}

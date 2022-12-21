package com.ogya.lokakarya.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.entity.HistoryBank;

public interface HistoryBankRepository extends JpaRepository<HistoryBank, Long> {
	List<HistoryBank> findByidHistoryBank(Long idHistoryBank);

	Page<HistoryBank> findAll(Pageable page);
}

package com.ogya.lokakarya.bankadm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ogya.lokakarya.bankadm.entity.HistoryBank;
public interface HistoryBankRepository extends JpaRepository<HistoryBank, Long> {
	List<HistoryBank> findByidHistoryBank(Long idHistoryBank);
	List<HistoryBank> findByStatusKet(Byte statusKet);
	Page<HistoryBank> findAll(Pageable page);
	
	Page<HistoryBank> findByStatusKet(Byte statusKet, Pageable paging);
	@Query(value = "SELECT count(*) FROM HISTORY_BANK e WHERE e.STATUS_KET = 1 AND e.TANGGAL  between trunc(sysdate)\n"
			+ "      And trunc(sysdate) + interval '1' day - interval '1' second", nativeQuery = true)
	Long sumStatus1();
	
	@Query(value = "SELECT count(*) FROM HISTORY_BANK e WHERE e.STATUS_KET = 2 AND e.TANGGAL  between trunc(sysdate)\n"
			+ "      And trunc(sysdate) + interval '1' day - interval '1' second", nativeQuery = true)
	Long sumStatus2();
	
	@Query(value = "SELECT count(*) FROM HISTORY_BANK e WHERE e.STATUS_KET = 3 AND e.TANGGAL  between trunc(sysdate)\n"
			+ "      And trunc(sysdate) + interval '1' day - interval '1' second", nativeQuery = true)
	Long sumStatus3();
	
	@Query(value = "SELECT count(*) FROM HISTORY_BANK e WHERE e.STATUS_KET = 4 AND e.TANGGAL  between trunc(sysdate)\n"
			+ "      And trunc(sysdate) + interval '1' day - interval '1' second", nativeQuery = true)
	Long sumStatus4();
}
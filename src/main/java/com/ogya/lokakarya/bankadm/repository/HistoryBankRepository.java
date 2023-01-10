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
	
	@Query(value = "SELECT hb.ID_HISTORY_BANK , hb.TANGGAL , hb.NOREK , hb.STATUS_KET , hb.NAMA ,hb.UANG ,hb.NOREK_DITUJU ,mb.NAMA ,hb.NO_TLP  FROM HISTORY_BANK hb JOIN MASTER_BANK mb \r\n"
			+ "ON (hb.NOREK_DITUJU  = mb.NOREK) WHERE hb.STATUS_KET = ?1",
		    countQuery = "SELECT count(*) FROM HISTORY_BANK hb JOIN MASTER_BANK mb \r\n"
		    		+ "ON (hb.NOREK_DITUJU  = mb.NOREK) WHERE hb.STATUS_KET = ?1",
		    nativeQuery = true)
		  Page<HistoryBank> findByStatusKet2(Byte statusKet, Pageable paging);
	
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
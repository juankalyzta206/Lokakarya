package com.ogya.lokakarya.bankadm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	
	
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE status_ket = 1", nativeQuery = true)
	    List<HistoryBank> laporanSetor();
	 
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE status_ket = 2", nativeQuery = true)
	    List<HistoryBank> laporanTarik();
	 
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE status_ket = 3", nativeQuery = true)
	    List<HistoryBank> laporanTransfer();
	 
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE status_ket = 4", nativeQuery = true)
	    List<HistoryBank> laporanBayarTelepon();
	 
	 @Query(value = "SELECT * FROM HISTORY_BANK hb WHERE STATUS_KET = 4 AND TO_CHAR(TANGGAL, 'yyyy-MM-dd')= :tanggal", nativeQuery = true)
	    List<HistoryBank> laporanBayarTeleponToday(@Param("tanggal") String tanggal);
	 
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE :sortField LIKE '%' || :keyFilter || '%' ORDER BY :sortField :sortOrder",
	            countQuery = "SELECT count(*) FROM HISTORY_BANK",
	            nativeQuery = true)
	    Page<HistoryBank> findAllFilter(@Param("keyFilter") String keyfilter,  @Param("sortOrder") String sortOrder,@Param("sortField") String sortField, @Param("paging") Pageable paging);

	 @Query(value = "SELECT * FROM HISTORY_BANK hb ORDER BY ID_HISTORY_BANK DESC ", nativeQuery = true)
	 List<HistoryBank> findLastHistory();
	 

	 
	 
	 @Query(value="SELECT * FROM HISTORY_BANK WHERE EXTRACT(YEAR FROM TANGGAL)= :year "
				+ "AND EXTRACT(MONTH FROM TANGGAL)= :month "
				+ "AND EXTRACT(DAY FROM TANGGAL)= :date "
				+ "AND STATUS_KET = 2 "
				+ "ORDER BY TANGGAL", 
				nativeQuery = true)
		List<HistoryBank> newTarikDaily (@Param("year") Integer year, @Param("month") Integer month, @Param("date") Integer date);
		
		@Query(value="SELECT * FROM HISTORY_BANK WHERE EXTRACT(YEAR FROM TANGGAL)= :year "
				+ "AND EXTRACT(MONTH FROM TANGGAL)= :month "
				+ "AND STATUS_KET = 2 "
				+ "ORDER BY TANGGAL", 
				nativeQuery = true)
		List<HistoryBank> newTarikMonthly (@Param("year") Integer year, @Param("month") Integer month);
			 
		@Query(value="SELECT * FROM HISTORY_BANK WHERE EXTRACT(YEAR FROM TANGGAL)= :year "
				+ "AND EXTRACT(MONTH FROM TANGGAL)= :month "
				+ "AND EXTRACT(DAY FROM TANGGAL)= :date "
				+ "AND EXTRACT(DOW FROM TANGGAL)= :dayOfWeek "
				+ "AND STATUS_KET = 2 "
				+ "ORDER BY TANGGAL", 
				nativeQuery = true)
		List<HistoryBank> newTarikWeekly (@Param("year") Integer year, @Param("month") Integer month, @Param("date") Integer date, @Param("dayOfWeek") Integer dayOfWeek);

		
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE STATUS_KET=1 AND TO_CHAR(TANGGAL, 'yyyy-MM-dd')=:hari ORDER BY TANGGAL ASC", nativeQuery = true)
	 List<HistoryBank> setorDaily (@Param("hari") String hari);
	 
	 
	 @Query(value = "SELECT SUM(UANG) FROM HISTORY_BANK WHERE STATUS_KET=1  AND TO_CHAR(TANGGAL, 'yyyy-MM-dd')=:hari ORDER BY TANGGAL ASC", nativeQuery = true)
	 Long totalSetorHarian (@Param("hari") String hari);
	 
	 @Query(value = "SELECT COUNT(*) FROM HISTORY_BANK WHERE STATUS_KET=1  AND TO_CHAR(TANGGAL, 'yyyy-MM-dd')=:hari ORDER BY TANGGAL ASC", nativeQuery = true)
	 Long jumlahSetorHarian (@Param("hari") String hari);

	 
	 @Query(value = "SELECT * FROM HISTORY_BANK WHERE STATUS_KET=1 AND TANGGAL BETWEEN TO_DATE(:start, 'yyyy-MM-dd') AND TO_DATE(:end, 'yyyy-MM-dd') ORDER BY TANGGAL ASC", nativeQuery = true)
	 List<HistoryBank> setorRekap (@Param("start") String start, @Param("end") String end);
	 
	 @Query(value = "SELECT SUM(UANG) FROM HISTORY_BANK WHERE STATUS_KET=1 AND TANGGAL BETWEEN TO_DATE(:start, 'yyyy-MM-dd') AND TO_DATE(:end, 'yyyy-MM-dd') ORDER BY TANGGAL ASC", nativeQuery = true)
	 Long totalSetorRekap (@Param("start") String start, @Param("end") String end);
	 
	 @Query(value = "SELECT COUNT(*) FROM HISTORY_BANK WHERE STATUS_KET=1  AND TANGGAL BETWEEN TO_DATE(:start, 'yyyy-MM-dd') AND TO_DATE(:end, 'yyyy-MM-dd') ORDER BY TANGGAL ASC", nativeQuery = true)
	 Long jumlahSetorRekap (@Param("start") String start, @Param("end") String end);
	 
}



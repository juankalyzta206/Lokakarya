package com.ogya.lokakarya.telepon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;

public interface HistoryTelkomRepository extends JpaRepository<HistoryTelkom, Long> {
	
	@Query(value="SELECT * FROM HISTORY_TELKOM ORDER BY ID_HISTORY DESC", nativeQuery = true)
	List<HistoryTelkom> findLastHistoryTelkom();
	List<HistoryTelkom> findByIdHistory (Long idHistory);
	
	@Query(value="SELECT * FROM HISTORY_TELKOM WHERE ID_PELANGGAN = :idPelanggan ORDER BY TANGGAL_BAYAR DESC", nativeQuery=true)
	List<HistoryTelkom> dataTeleponById(@Param("idPelanggan") MasterPelanggan idPelanggan);
	
	@Query(value = "SELECT * FROM HISTORY_TELKOM WHERE TO_CHAR(TANGGAL_BAYAR, 'yyyy-MM-dd')=:hari ORDER BY TANGGAL_BAYAR ASC", nativeQuery = true)
	 List<HistoryTelkom> lunasDaily (@Param("hari") String hari);
	
	@Query(value = "SELECT * FROM HISTORY_TELKOM WHERE TANGGAL_BAYAR BETWEEN TO_DATE(:start, 'yyyy-MM-dd') AND TO_DATE(:end, 'yyyy-MM-dd') ORDER BY TANGGAL_BAYAR ASC", nativeQuery = true)
	 List<HistoryTelkom> lunasRekap (@Param("start") String start, @Param("end") String end);
	
}

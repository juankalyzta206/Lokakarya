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
}

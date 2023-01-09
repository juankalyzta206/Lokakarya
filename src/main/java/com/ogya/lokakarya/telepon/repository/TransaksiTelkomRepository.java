package com.ogya.lokakarya.telepon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;


public interface TransaksiTelkomRepository extends JpaRepository<TransaksiTelkom, Long> {

	
	@Query(value = "SELECT SUM(UANG) FROM TRANSAKSI_TELKOM  WHERE STATUS = 1", nativeQuery = true)
	Long sumAll();
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM e WHERE e.STATUS = 1 ", nativeQuery = true)
	List<TransaksiTelkom> findStatus1();
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM WHERE ID_PELANGGAN = :idPelanggan", nativeQuery = true)
	List<TransaksiTelkom> findByTagihanPelanggan(@Param("idPelanggan") Long idPelanggan);
	
	@Query(value = "SELECT SUM(UANG) FROM TRANSAKSI_TELKOM WHERE ID_PELANGGAN = :idPelanggan AND STATUS = 1", nativeQuery = true)
	Long tagihanTelpon (@Param("idPelanggan") Long idPelanggan);
	
	@Query(value = "SELECT STATUS FROM TRANSAKSI_TELKOM WHERE ID_PELANGGAN = :idPelanggan", nativeQuery = true)
	List<Integer> statusTagihan (@Param("idPelanggan") Long idPelanggan);

	
}

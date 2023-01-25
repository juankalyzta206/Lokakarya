package com.ogya.lokakarya.repository.telepon;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.entity.telepon.MasterPelanggan;
import com.ogya.lokakarya.entity.telepon.TransaksiTelkom;


public interface TransaksiTelkomRepository extends JpaRepository<TransaksiTelkom, Long> {

	
	@Query(value = "SELECT SUM(UANG) FROM TRANSAKSI_TELKOM  WHERE STATUS = 1", nativeQuery = true)
	Long sumAll();
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM e WHERE e.STATUS = 1 ", nativeQuery = true)
	List<TransaksiTelkom> findStatus1();
	
	@Query(value = "SELECT e FROM TransaksiTelkom e WHERE e.status = 1 ")
	List<TransaksiTelkom> findStatus1(Sort sort);
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM WHERE ID_PELANGGAN = :idPelanggan", nativeQuery = true)
	List<TransaksiTelkom> findByTagihanPelanggan(@Param("idPelanggan") Long idPelanggan);
	
	@Query(value = "SELECT SUM(UANG) FROM TRANSAKSI_TELKOM WHERE ID_PELANGGAN = :idPelanggan AND STATUS = 1", nativeQuery = true)
	Long tagihanTelpon (@Param("idPelanggan") Long idPelanggan);
	
	@Query(value = "SELECT STATUS FROM TRANSAKSI_TELKOM WHERE ID_PELANGGAN = :idPelanggan", nativeQuery = true)
	List<Integer> statusTagihan (@Param("idPelanggan") Long idPelanggan);
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM  WHERE STATUS = 1 ", nativeQuery = true)
	Page<TransaksiTelkom> findAllWithStatus1(Pageable page);
	
	TransaksiTelkom findByidPelanggan(MasterPelanggan idPelanggan);
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM WHERE STATUS = 1 AND BULAN_TAGIHAN = :bulan AND TAHUN_TAGIHAN = :tahun", nativeQuery = true)
	List<TransaksiTelkom> laporanPenunggakanMonthly(@Param("bulan")  String bulan,@Param("tahun")  String tahun);
	
	@Query(value = "SELECT COUNT(*) FROM TRANSAKSI_TELKOM WHERE STATUS = 1 AND BULAN_TAGIHAN = :bulan  AND TAHUN_TAGIHAN = :tahun", nativeQuery = true)
	Long jumlahLaporanPenunggakanMonthly(@Param("bulan")  String bulan,@Param("tahun")  String tahun);
	
	@Query(value = "SELECT SUM(UANG) FROM TRANSAKSI_TELKOM WHERE STATUS = 1 AND BULAN_TAGIHAN = :bulan  AND TAHUN_TAGIHAN = :tahun", nativeQuery = true)
	Long totalLaporanPenunggakanMonthly(@Param("bulan")  String bulan,@Param("tahun")  String tahun);
	
	@Query(value = "SELECT * FROM TRANSAKSI_TELKOM WHERE STATUS = 1 AND BULAN_TAGIHAN BETWEEN :bawal AND :bakhir;", nativeQuery = true)
	List<TransaksiTelkom> laporanPenunggakanYearly(@Param("bawal")  String bawal, @Param("bakhir") String bakhir);
	
	@Query(value = "SELECT COUNT(*) FROM TRANSAKSI_TELKOM WHERE STATUS = 1 AND BULAN_TAGIHAN BETWEEN :bawal AND :bakhir;", nativeQuery = true)
	Long jumlahLaporanPenunggakanYearly(@Param("bawal")  String bawal, @Param("bakhir") String bakhir);
	
	@Query(value = "SELECT SUM(UANG) FROM TRANSAKSI_TELKOM WHERE STATUS = 1 AND BULAN_TAGIHAN BETWEEN :bawal AND :bakhir;", nativeQuery = true)
	Long totalLaporanPenunggakanYearly(@Param("bawal")  String bawal, @Param("bakhir") String bakhir);
}

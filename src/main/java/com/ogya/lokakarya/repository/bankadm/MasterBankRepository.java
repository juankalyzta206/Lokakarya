package com.ogya.lokakarya.repository.bankadm;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogya.lokakarya.entity.bankadm.MasterBank;

public interface MasterBankRepository extends JpaRepository<MasterBank, Long> {
	
	List<MasterBank> findByNorek(Long norek);
	List<MasterBank> findByNorekAndNama (Long norek, String nama);
	List<MasterBank> deleteByNorekAndNama (Long norek, String nama);
	Page<MasterBank> findAll(Pageable page);
	
	@Query(value="SELECT COUNT(*) FROM HISTORY_BANK hb WHERE hb.NOREK = :norek", 
			nativeQuery = true)
	Long isExistHistoyBank(@Param("norek") Long norek);
	
	@Query(value="SELECT COUNT(*) FROM MASTER_BANK mb WHERE mb.NOREK = :norek", 
			nativeQuery = true)
	Long isExistMasterBank(@Param("norek") Long norek);
	

}



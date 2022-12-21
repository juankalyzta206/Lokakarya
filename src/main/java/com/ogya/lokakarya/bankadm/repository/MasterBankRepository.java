package com.ogya.lokakarya.bankadm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.bankadm.entity.MasterBank;

public interface MasterBankRepository extends JpaRepository<MasterBank, Long> {
	
	List<MasterBank> findByNorek(Long norek);
	List<MasterBank> findByNorekAndNama (Long norek, String nama);
	List<MasterBank> deleteByNorekAndNama (Long norek, String nama);
	Page<MasterBank> findAll(Pageable page);
	


}

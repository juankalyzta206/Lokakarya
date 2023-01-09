package com.ogya.lokakarya.bankadm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.entity.TransaksiNasabah;

public interface TransaksiNasabahRepository extends JpaRepository<TransaksiNasabah, Long>{
	TransaksiNasabah findByMasterBank (MasterBank masterBank);

}

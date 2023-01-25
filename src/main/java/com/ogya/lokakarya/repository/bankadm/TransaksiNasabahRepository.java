package com.ogya.lokakarya.repository.bankadm;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogya.lokakarya.entity.bankadm.MasterBank;
import com.ogya.lokakarya.entity.bankadm.TransaksiNasabah;

public interface TransaksiNasabahRepository extends JpaRepository<TransaksiNasabah, Long>{
	TransaksiNasabah findByMasterBank (MasterBank masterBank);

}

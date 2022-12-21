package com.ogya.lokakarya.bankadm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.entity.TransaksiNasabah;
import com.ogya.lokakarya.bankadm.repository.MasterBankRepository;
import com.ogya.lokakarya.bankadm.repository.TransaksiNasabahRepository;
import com.ogya.lokakarya.bankadm.wrapper.TransaksiNasabahWrapper;

@Service
@Transactional
public class TransaksiNasabahService {
	@Autowired
	TransaksiNasabahRepository transaksiNasabahRepository;
	@Autowired
	MasterBankRepository masterBankRepository;
	
	public TransaksiNasabahWrapper getByidTransaksiNasabah(Long idTransaksiNasabah) {
		TransaksiNasabah transaksinasabah = transaksiNasabahRepository.getReferenceById(idTransaksiNasabah);
		return toWrapper(transaksinasabah);
	}
	
	private TransaksiNasabahWrapper toWrapper(TransaksiNasabah entity) {
		TransaksiNasabahWrapper wrapper = new TransaksiNasabahWrapper();
		wrapper.setIdTransaksiNasabah(entity.getIdTransaksiNasabah());
		wrapper.setNorek(entity.getRekening() != null ? entity.getRekening().getNorek() : null);
		wrapper.setNama(entity.getRekening() != null ? entity.getRekening().getNama() : null);
		wrapper.setTanggel(entity.getTanggel());
		wrapper.setStatus(entity.getStatus());
		wrapper.setUang(entity.getUang());
		wrapper.setStatusKet(entity.getStatusKet());
		wrapper.setNoRekTujuan(entity.getNoRekTujuan());
		wrapper.setNo_tlp(entity.getNo_tlp());
		return wrapper;
	}
	
	private List<TransaksiNasabahWrapper> toWrapperList(List<TransaksiNasabah> entityList){
		List<TransaksiNasabahWrapper> wrapperList = new ArrayList<TransaksiNasabahWrapper>();
		for(TransaksiNasabah entity : entityList) {
			TransaksiNasabahWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public List<TransaksiNasabahWrapper> findAll() {
		List<TransaksiNasabah> employeeList = transaksiNasabahRepository.findAll(Sort.by(Order.by("idTransaksiNasabah")).descending());
		return toWrapperList(employeeList);
	}
	
	private TransaksiNasabah toEntity(TransaksiNasabahWrapper wrapper) {
		TransaksiNasabah entity = new TransaksiNasabah();
		if (wrapper.getIdTransaksiNasabah() != null) {
			entity = transaksiNasabahRepository.getReferenceById(wrapper.getIdTransaksiNasabah());
		}
		entity.setIdTransaksiNasabah(wrapper.getIdTransaksiNasabah());
		Optional<MasterBank> optionalRek = masterBankRepository.findById(wrapper.getNorek());
		MasterBank rekening = optionalRek.isPresent() ? optionalRek.get() : null;
		entity.setRekening(rekening);
		entity.setTanggel(wrapper.getTanggel());
		entity.setStatus(wrapper.getStatus());
		entity.setUang(wrapper.getUang());
		entity.setStatusKet(wrapper.getStatusKet());
		entity.setNoRekTujuan(wrapper.getNoRekTujuan());
		entity.setNo_tlp(wrapper.getNo_tlp());
		return entity;
	}
	
	public TransaksiNasabahWrapper save(TransaksiNasabahWrapper wrapper) {
		TransaksiNasabah employee = transaksiNasabahRepository.save(toEntity(wrapper));
		return toWrapper(employee);
	}
	
	public void delete(Long idTransaksiNasabah) {
		transaksiNasabahRepository.deleteById(idTransaksiNasabah);
	}
}

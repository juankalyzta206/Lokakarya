package com.ogya.lokakarya.bankadm.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.repository.MasterBankRepository;
import com.ogya.lokakarya.bankadm.wrapper.MasterBankWrapper;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.util.PaginationList;


@Service
@Transactional
public class MasterBankService {
	@Autowired
	MasterBankRepository masterBankRepository;
	
	public MasterBankWrapper getByNoRek(Long norek) {
		MasterBank masterbank = masterBankRepository.getReferenceById(norek);
		return toWrapper(masterbank);
	}
	
	private MasterBankWrapper toWrapper(MasterBank entity) {
		MasterBankWrapper wrapper = new MasterBankWrapper();
		wrapper.setNorek(entity.getNorek());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setNotlp(entity.getNotlp());
		wrapper.setSaldo(entity.getSaldo());
		wrapper.setUserId(entity.getUserId());
		return wrapper;
	}
	
	private List<MasterBankWrapper> toWrapperList(List<MasterBank> entityList){
		List<MasterBankWrapper> wrapperList = new ArrayList<MasterBankWrapper>();
		for(MasterBank entity : entityList) {
			MasterBankWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public List<MasterBankWrapper> findAll() {
		List<MasterBank> employeeList = masterBankRepository.findAll(Sort.by(Order.by("norek")).descending());
		return toWrapperList(employeeList);
	}
	
	private MasterBank toEntity(MasterBankWrapper wrapper) {
		MasterBank entity = new MasterBank();
		if (wrapper.getNorek() != null) {
			entity = masterBankRepository.getReferenceById(wrapper.getNorek());
		}
		entity.setNorek(wrapper.getNorek());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setNotlp(wrapper.getNotlp());
		entity.setSaldo(wrapper.getSaldo());
		entity.setUserId(wrapper.getUserId());
		return entity;
	}
	
	public MasterBankWrapper save(MasterBankWrapper wrapper) {
		MasterBank employee = masterBankRepository.save(toEntity(wrapper));
		return toWrapper(employee);
	}
	
	public void delete(Long norek) {
		if(masterBankRepository.isExistMasterBank(norek) != 0)
				if (masterBankRepository.isExistHistoyBank(norek) == 0) {
					masterBankRepository.deleteById(norek);
				} else {
					throw new BusinessException("NASABAH cannot deleted. REK. NUMBER is still used in the HISTORY table");
				}else {
					throw new BusinessException("NASABAH with REK. NUMBER inputed is not Exist!");
				}
	
}
	
	public PaginationList<MasterBankWrapper, MasterBank> findAllWithPagination(int page, int size){
		Pageable paging = PageRequest.of(page, size);
		Page<MasterBank> bankPage = masterBankRepository.findAll(paging);
		List<MasterBank> bankList =  bankPage.getContent();
		List<MasterBankWrapper> bookWrapperList = toWrapperList(bankList);
		return new PaginationList<MasterBankWrapper, MasterBank>(bookWrapperList, bankPage);
	}
}


package com.ogya.lokakarya.bankadm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.repository.MasterBankRepository;
import com.ogya.lokakarya.bankadm.wrapper.HistoryBankWrapper;
import com.ogya.lokakarya.util.PaginationList;

@Service
@Transactional
public class HistoryBankService {
@Autowired
HistoryBankRepository historyBankRepository;
@Autowired
MasterBankRepository masterBankRepository;

public HistoryBankWrapper getByidHistoryBank(Long idHistoryBank) {
	HistoryBank historybank = historyBankRepository.getReferenceById(idHistoryBank);
	return toWrapper(historybank);
}
public Long sumStatus1() {
	Long historybank = historyBankRepository.sumStatus1();
	return historybank;
}

public Long sumStatus2() {
	Long historybank = historyBankRepository.sumStatus2();
	return historybank;
}

public Long sumStatus3() {
	Long historybank = historyBankRepository.sumStatus3();
	return historybank;
}

public Long sumStatus4() {
	Long historybank = historyBankRepository.sumStatus4();
	return historybank;
}

private HistoryBankWrapper toWrapper(HistoryBank entity) {
	HistoryBankWrapper wrapper = new HistoryBankWrapper();
	wrapper.setIdHistoryBank(entity.getIdHistoryBank());
	wrapper.setNorek(entity.getRekening() != null ? entity.getRekening().getNorek() : null);
	wrapper.setNama(entity.getNama());
	wrapper.setTanggal(entity.getTanggal());
	wrapper.setUang(entity.getUang());
	wrapper.setStatusKet(entity.getStatusKet());
	wrapper.setNoRekTujuan(entity.getNoRekTujuan());
	wrapper.setNoTlp(entity.getNoTlp());
	return wrapper;
}

private List<HistoryBankWrapper> toWrapperList(List<HistoryBank> entityList){
	List<HistoryBankWrapper> wrapperList = new ArrayList<HistoryBankWrapper>();
	for( HistoryBank entity : entityList) {
		HistoryBankWrapper wrapper = toWrapper(entity);
		wrapperList.add(wrapper);
	}
	return wrapperList;
}

public List<HistoryBankWrapper> findAll() {
	List<HistoryBank> employeeList = historyBankRepository.findAll(Sort.by(Order.by("idHistoryBank")).descending());
	return toWrapperList(employeeList);
}

private HistoryBank toEntity(HistoryBankWrapper wrapper) {
	HistoryBank entity = new HistoryBank();
	if (wrapper.getIdHistoryBank() != null) {
		entity = historyBankRepository.getReferenceById(wrapper.getIdHistoryBank());
	}
	entity.setIdHistoryBank(wrapper.getIdHistoryBank());
	Optional<MasterBank> optionalRek = masterBankRepository.findById(wrapper.getNorek());
	MasterBank rekening = optionalRek.isPresent() ? optionalRek.get() : null;
	entity.setRekening(rekening);
	entity.setTanggal(wrapper.getTanggal());
	entity.setUang(wrapper.getUang());
	entity.setStatusKet(wrapper.getStatusKet());
	entity.setNoRekTujuan(wrapper.getNoRekTujuan());
	entity.setNoTlp(wrapper.getNoTlp());
	return entity;
}

public HistoryBankWrapper save(HistoryBankWrapper wrapper) {
	HistoryBank employee = historyBankRepository.save(toEntity(wrapper));
	return toWrapper(employee);
}

public void delete(Long idHistoryBank) {
	historyBankRepository.deleteById(idHistoryBank);
}

public List<HistoryBankWrapper> getBystatusKet(Byte statusKet) {
	List<HistoryBank> historybank =  historyBankRepository.findByStatusKet(statusKet);
	return toWrapperList(historybank);
}

public PaginationList<HistoryBankWrapper, HistoryBank> findAllWithPagination(int page, int size){
	Pageable paging = PageRequest.of(page, size);
	Page<HistoryBank> historyPage = historyBankRepository.findAll(paging);
	List<HistoryBank> historyList =  historyPage.getContent();
	List<HistoryBankWrapper> historyWrapperList = toWrapperList(historyList);
	return new PaginationList<HistoryBankWrapper, HistoryBank>(historyWrapperList, historyPage);
}

public PaginationList<HistoryBankWrapper, HistoryBank> findByStatusKetPagination(Byte statusKet, int page, int size) {
	Pageable paging = PageRequest.of(page, size);
	Page<HistoryBank> historyPage = historyBankRepository.findByStatusKet( statusKet, paging);
	List<HistoryBank> historyList = historyPage.getContent();
	List<HistoryBankWrapper> historyWrapperList = toWrapperList(historyList);
	return new PaginationList<HistoryBankWrapper, HistoryBank>(historyWrapperList, historyPage);
}

}

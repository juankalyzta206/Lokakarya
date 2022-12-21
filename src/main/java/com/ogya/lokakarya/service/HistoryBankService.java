package com.ogya.lokakarya.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.entity.HistoryBank;
import com.ogya.lokakarya.entity.MasterBank;
import com.ogya.lokakarya.repository.HistoryBankRepository;
import com.ogya.lokakarya.repository.MasterBankRepository;
import com.ogya.lokakarya.wrapper.HistoryBankWrapper;

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

private HistoryBankWrapper toWrapper(HistoryBank entity) {
	HistoryBankWrapper wrapper = new HistoryBankWrapper();
	wrapper.setIdHistoryBank(entity.getIdHistoryBank());
	wrapper.setNorek(entity.getRekening() != null ? entity.getRekening().getNorek() : null);
	wrapper.setNama(entity.getNama());
	wrapper.setTanggel(entity.getTanggel());
	wrapper.setUang(entity.getUang());
	wrapper.setStatusKet(entity.getStatusKet());
	wrapper.setNoRekTujuan(entity.getNoRekTujuan());
	wrapper.setNo_tlp(entity.getNo_tlp());
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
	if (wrapper.getNorek() != null) {
		entity = historyBankRepository.getReferenceById(wrapper.getIdHistoryBank());
	}
	entity.setIdHistoryBank(wrapper.getIdHistoryBank());
	Optional<MasterBank> optionalRek = masterBankRepository.findById(wrapper.getNorek());
	MasterBank rekening = optionalRek.isPresent() ? optionalRek.get() : null;
	entity.setRekening(rekening);
	entity.setTanggel(wrapper.getTanggel());
	entity.setUang(wrapper.getUang());
	entity.setStatusKet(wrapper.getStatusKet());
	entity.setNoRekTujuan(wrapper.getNoRekTujuan());
	entity.setNo_tlp(wrapper.getNo_tlp());
	return entity;
}

public HistoryBankWrapper save(HistoryBankWrapper wrapper) {
	HistoryBank employee = historyBankRepository.save(toEntity(wrapper));
	return toWrapper(employee);
}

public void delete(Long idHistoryBank) {
	historyBankRepository.deleteById(idHistoryBank);
}

}

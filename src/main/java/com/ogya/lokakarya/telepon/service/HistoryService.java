package com.ogya.lokakarya.telepon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.repository.HistoryRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.wrapper.HistoryWrapper;
import com.ogya.lokakarya.util.PaginationList;

@Service
@Transactional
public class HistoryService {
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	public Long sumAll() {
		Long sumAll = historyRepository.sumAll();
		return sumAll;
	}
	//service untuk menampilkan semua list
	public List<HistoryWrapper> findAll(){
		List<HistoryTelkom> historyTelkomList = historyRepository.findAll();
		return toWrapperList(historyTelkomList);
	}
	//service untuk memasukkan/mengubah entity
	public HistoryWrapper save(HistoryWrapper wrapper) {
		HistoryTelkom historyTelkom = historyRepository.save(toEntity(wrapper));
		return toWrapper(historyTelkom);
	}
	//service untuk menghapus entity
	public void deleteById(Long historyId) {
		historyRepository.deleteById(historyId);
	}
	
	
	//method dalam service untuk mengubah entity ke wrapper
	private HistoryWrapper toWrapper(HistoryTelkom entity) {
		HistoryWrapper wrapper = new HistoryWrapper();
		wrapper.setIdHistory(entity.getIdHistory());
		wrapper.setTanggalBayar(entity.getTanggalBayar());
		wrapper.setBulanTagihan(entity.getBulanTagihan());
		wrapper.setTahunTagihan(entity.getTahunTagihan());
		wrapper.setUang(entity.getUang());
		wrapper.setIdPelanggan(entity.getIdPelanggan() != null ? entity.getIdPelanggan().getIdPelanggan() : null);
//		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
//		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
//		wrapper.setNama(masterPelanggan.getNama());
		return wrapper;
	}
	//method dalam service untuk memasukkan nilai kedalam entity
	private HistoryTelkom toEntity(HistoryWrapper wrapper) {
		HistoryTelkom entity = new HistoryTelkom();
		if (wrapper.getIdHistory() != null) {
			entity = historyRepository.getReferenceById(wrapper.getIdHistory());
		}
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
		entity.setIdPelanggan(masterPelanggan);
		entity.setBulanTagihan(wrapper.getBulanTagihan());
		entity.setIdHistory(wrapper.getIdHistory());
		entity.setTahunTagihan(wrapper.getTahunTagihan());
		entity.setTanggalBayar(wrapper.getTanggalBayar());
		entity.setUang(wrapper.getUang());
		return entity;
	}
	//method dalam service untuk menampilkan semua list
	private List<HistoryWrapper> toWrapperList(List<HistoryTelkom> entityList) {
		List<HistoryWrapper> wrapperList = new ArrayList<HistoryWrapper>();
		for (HistoryTelkom entity : entityList) {
			HistoryWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	public PaginationList<HistoryWrapper, HistoryTelkom> findAllWithPagination(int page, int size){
		Pageable paging = PageRequest.of(page, size, Sort.by("tanggalBayar").descending());
		Page<HistoryTelkom> historyPage = historyRepository.findAll(paging);
		List<HistoryTelkom> historyList =  historyPage.getContent();
		List<HistoryWrapper> historyWrapperList = toWrapperList(historyList);
		return new PaginationList<HistoryWrapper, HistoryTelkom>(historyWrapperList, historyPage);
	}
}

package com.ogya.lokakarya.telepon.service;

import java.util.ArrayList;
import java.util.Date;
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

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;
import com.ogya.lokakarya.telepon.repository.HistoryRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.telepon.wrapper.HistoryWrapper;
import com.ogya.lokakarya.telepon.wrapper.TransaksiTelkomWrapper;
import com.ogya.lokakarya.util.PaginationList;

@Service
@Transactional
public class TransaksiTelkomService {

	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	HistoryRepository historyRepository;
	
	public Long sumAll() {
		Long sumAll = transaksiTelkomRepository.sumAll();
		return sumAll;
	}
	
	
	//service untuk menampilkan semua list
	public List<TransaksiTelkomWrapper> findAll(){
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository.findAll(Sort.by(Order.by("idTransaksi")).descending());
		return toWrapperList(transaksiTelkomList);
	}
	public List<TransaksiTelkomWrapper> findAllSortByMonthAndYear(){
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository.findAll(Sort.by(Order.by("tahunTagihan")).descending().and(Sort.by(Order.by("bulanTagihan")).descending()));
		return toWrapperList(transaksiTelkomList);
	}
	public List<TransaksiTelkomWrapper> findAllStatus1(){
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository.findStatus1(Sort.by(Order.by("idTransaksi")).descending());
		return toWrapperList(transaksiTelkomList);
	}
	//service untuk memasukkan/mengubah entity 
	public TransaksiTelkomWrapper save(TransaksiTelkomWrapper wrapper) {
		TransaksiTelkom transaksiTelkom = transaksiTelkomRepository.save(toEntity(wrapper));
		//kondisional jika nilai status 2, maka service juga akan memasukkan nilai kedalam tabel history
		if(wrapper.getStatus() == 2) {
			HistoryWrapper historyWrapper = new HistoryWrapper();
			Date date = new Date();
			historyWrapper.setBulanTagihan(wrapper.getBulanTagihan());
			historyWrapper.setTahunTagihan(wrapper.getTahunTagihan());
			historyWrapper.setIdPelanggan(wrapper.getIdPelanggan());
			historyWrapper.setTanggalBayar(date);
			historyWrapper.setUang(wrapper.getUang());
			historyRepository.save(toEntity(historyWrapper));
		}
		return toWrapper(transaksiTelkom);
	}
	//service untuk menghapus entity
	public void deleteById(Long transaksiId) {
		transaksiTelkomRepository.deleteById(transaksiId);
	}
	//method dalam service untuk mengubah entity ke wrapper
	private TransaksiTelkomWrapper toWrapper(TransaksiTelkom entity) {
		TransaksiTelkomWrapper wrapper = new TransaksiTelkomWrapper();
		wrapper.setBulanTagihan(entity.getBulanTagihan());
		wrapper.setTahunTagihan(entity.getTahunTagihan());
		wrapper.setUang(entity.getUang());
		wrapper.setStatus(entity.getStatus());
		wrapper.setIdTransaksi(entity.getIdTransaksi());
		wrapper.setIdPelanggan(entity.getIdPelanggan() != null? entity.getIdPelanggan().getIdPelanggan() : null);
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
		wrapper.setNama(masterPelanggan.getNama());
		return wrapper;
	}
	//method dalam service untuk memasukkan nilai kedalam entity
	private TransaksiTelkom toEntity(TransaksiTelkomWrapper wrapper) {
		TransaksiTelkom entity = new TransaksiTelkom();
		if (wrapper.getIdTransaksi() != null) {
			entity = transaksiTelkomRepository.getReferenceById(wrapper.getIdTransaksi());
			if(entity.getIdPelanggan().getIdPelanggan().equals(wrapper.getIdPelanggan()) && entity.getTahunTagihan().equals(wrapper.getTahunTagihan()) && entity.getBulanTagihan() != wrapper.getBulanTagihan()) {
				List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository.findByTagihanPelanggan(wrapper.getIdPelanggan());
				for(TransaksiTelkom entity1 : transaksiTelkomList) {
					if(entity1.getIdPelanggan().getIdPelanggan().equals(wrapper.getIdPelanggan()) && entity1.getTahunTagihan().equals(wrapper.getTahunTagihan())  && entity1.getBulanTagihan().equals(wrapper.getBulanTagihan())) {
						throw new BusinessException("bulan tagihan tidak boleh sama");
					}
				}
			}
		}
		else {
			List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository.findByTagihanPelanggan(wrapper.getIdPelanggan());
			for(TransaksiTelkom entity1 : transaksiTelkomList) {
				if(entity1.getIdPelanggan().getIdPelanggan().equals(wrapper.getIdPelanggan()) && entity1.getTahunTagihan().equals(wrapper.getTahunTagihan())  && entity1.getBulanTagihan().equals(wrapper.getBulanTagihan())) {
					throw new BusinessException("bulan tagihan tidak boleh sama");
				}
			}
		}
		if(wrapper.getBulanTagihan() == null) {
			throw new BusinessException("bulan tagihan tidak boleh null");
		}
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null ;
		entity.setIdPelanggan(masterPelanggan);
		
		entity.setBulanTagihan(wrapper.getBulanTagihan());
		entity.setTahunTagihan(wrapper.getTahunTagihan());
		entity.setUang(wrapper.getUang());
		if(wrapper.getStatus() == 1 || wrapper.getStatus() == 2) {
		entity.setStatus(wrapper.getStatus());
		}
		else {
			throw new BusinessException("status harus berisi 1(belumbayar) atau 2(lunas)");
		}
		return entity;
	}
	//method dalam service untuk memasukkan nilai kedalam entity
	private HistoryTelkom toEntity(HistoryWrapper wrapper) {
		HistoryTelkom entity = new HistoryTelkom();
		if (wrapper.getIdHistory() != null) {
			entity = historyRepository.getReferenceById(wrapper.getIdHistory());
		}
		if (wrapper.getBulanTagihan() == null) {
			throw new BusinessException("Bulan tagihan tidak boleh null");
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
	private List<TransaksiTelkomWrapper> toWrapperList(List<TransaksiTelkom> entityList) {
		List<TransaksiTelkomWrapper> wrapperList = new ArrayList<TransaksiTelkomWrapper>();
		for (TransaksiTelkom entity : entityList) {
			TransaksiTelkomWrapper wrapper = toWrapper(entity);			
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	public PaginationList<TransaksiTelkomWrapper, TransaksiTelkom> findAllWithPagination(int page, int size){
		Pageable paging = PageRequest.of(page, size);
		Page<TransaksiTelkom> transaksiTelkomPage = transaksiTelkomRepository.findAll(paging);
		List<TransaksiTelkom> transaksiTelkomList =  transaksiTelkomPage.getContent();
		List<TransaksiTelkomWrapper> transaksiTelkomWrapperList = toWrapperList(transaksiTelkomList);
		return new PaginationList<TransaksiTelkomWrapper, TransaksiTelkom>(transaksiTelkomWrapperList, transaksiTelkomPage);
	}
}

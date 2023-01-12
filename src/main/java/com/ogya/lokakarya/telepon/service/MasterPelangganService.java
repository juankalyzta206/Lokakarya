package com.ogya.lokakarya.telepon.service;

import java.io.ByteArrayInputStream;
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

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.helper.ExcelHelperMasterPelanggan;
import com.ogya.lokakarya.telepon.repository.HistoryRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.telepon.wrapper.MasterPelangganWrapper;
import com.ogya.lokakarya.telepon.wrapper.TeleponFilterWrapper;
import com.ogya.lokakarya.telepon.wrapper.TeleponPagingRequestWrapper;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;



@Service
@Transactional
public class MasterPelangganService {
	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	UsersRepository usersRepository;
	
	//service untuk menampilkan semua list
	public List<MasterPelangganWrapper> findAll(){
		List<MasterPelanggan> masterPelangganList = masterPelangganRepository.findAll(Sort.by(Order.by("idPelanggan")).descending());
		return toWrapperList(masterPelangganList);
	}
	//service untuk memasukkan/mengubah entity 
	public MasterPelangganWrapper save(MasterPelangganWrapper wrapper) {
		MasterPelanggan masterPelanggan = masterPelangganRepository.save(toEntity(wrapper));
		return toWrapper(masterPelanggan);
	}
	//service untuk menghapus entity
	public void deleteById(Long masterPelangganId) {
		if( transaksiTelkomRepository.findById(masterPelangganId).isPresent()) {
			throw new BusinessException("id tidak bisa dihapus");
		}
		else {
			masterPelangganRepository.deleteById(masterPelangganId);
			
		}
	}
	//method dalam service untuk mengubah entity ke wrapper
	private MasterPelangganWrapper toWrapper(MasterPelanggan entity) {
		MasterPelangganWrapper wrapper = new MasterPelangganWrapper();
		wrapper.setIdPelanggan(entity.getIdPelanggan());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setNama(entity.getNama());
		wrapper.setNoTelp(entity.getNoTelp());
		wrapper.setUserId(entity.getUsers() != null ? entity.getUsers().getUserId() : null);
		return wrapper;
	}
	//method dalam service untuk memasukkan nilai kedalam entity
	private MasterPelanggan toEntity(MasterPelangganWrapper wrapper) {
		MasterPelanggan entity = new MasterPelanggan();
		if (wrapper.getIdPelanggan() != null) {
			entity = masterPelangganRepository.getReferenceById(wrapper.getIdPelanggan());
		}
		Optional<Users> optionalUsers = usersRepository.findById(wrapper.getUserId());
		Users users = optionalUsers.isPresent() ? optionalUsers.get() : null;
		entity.setUsers(users);
		entity.setIdPelanggan(wrapper.getIdPelanggan());
		entity.setAlamat(wrapper.getAlamat());
		entity.setNama(wrapper.getNama());
		entity.setNoTelp(wrapper.getNoTelp());
		return entity;
	}
	//method dalam service untuk menampilkan semua list
	private List<MasterPelangganWrapper> toWrapperList(List<MasterPelanggan> entityList) {
		List<MasterPelangganWrapper> wrapperList = new ArrayList<MasterPelangganWrapper>();
		for (MasterPelanggan entity : entityList) {
			MasterPelangganWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public PaginationList<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(int page, int size){
		Pageable paging = PageRequest.of(page, size);
		Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAll(paging);
		List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
		List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
		return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
	}
//	public PaginationList<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(int page, int size,Long idPelanggan){
//		Pageable paging = PageRequest.of(page, size);
//		Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAll(paging,idPelanggan);
//		List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
//		List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
//		return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
//	}
//	public <T>PaginationList<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(int page, int size,String filter,T value ){
//		Pageable paging = PageRequest.of(page, size);
//		if(filter.equals("idPelanggan")) {
//			Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAllIdPelanggan(paging,value);
//			List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
//			List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
//			return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
//		}
//		else if(filter.equals("nama")) {
//			Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAllName(paging,value);
//			List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
//			List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
//			return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
//		}
//		else if(filter.equals("alamat")) {
//			Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAllAlamat(paging,value);
//			List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
//			List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
//			return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
//		}
//		else if(filter.equals("noTelp")) {
//			Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAllNotelp(paging,value);
//			List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
//			List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
//			return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
//		}
//		else {
//			Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAllUserId(paging,value);
//			List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
//			List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
//			return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
//		}
//		Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAll(paging,value);
//	}
	public PaginationList<MasterPelangganWrapper, MasterPelanggan> findAllWithPaginationFilter(TeleponPagingRequestWrapper wrapper){
		Pageable paging;
		if (wrapper.getSortOrder() == "ASC") {
			paging = PageRequest.of(wrapper.getPage(), wrapper.getSize(),Sort.by(Order.by(wrapper.getSortField())).ascending());
		} else {
			paging = PageRequest.of(wrapper.getPage(), wrapper.getSize(),Sort.by(Order.by(wrapper.getSortField())).descending());
		}
		List<TeleponFilterWrapper> filterWrapper = wrapper.getFilters();
		Long idPelanggan = 0L;
		String nama = null;
		Long noTelp = null;
		String alamat = null;
		Long userId = 0L;
		for (int i=0; i<filterWrapper.size(); i++) {
			switch(wrapper.getFilters().get(i).getName().toLowerCase()) {
			  case "idpelanggan":
				  idPelanggan = Long.valueOf(wrapper.getFilters().get(i).getValue()) ;
				  break;
			  case "nama":
				 nama = wrapper.getFilters().get(i).getValue() ;
				 break;
			  case "notelp":
				  noTelp = Long.valueOf(wrapper.getFilters().get(i).getValue()) ; 
				  break;
			  case "alamat": 
				  alamat = wrapper.getFilters().get(i).getValue() ;
				  break;
			  case "userid":
				  userId =   Long.valueOf(wrapper.getFilters().get(i).getValue()) ; 
				  break;
			  default:
			    // code block
			}
		}
		Optional<Users> optionalUser = usersRepository.findById(userId);
		Users users = optionalUser.isPresent() ? optionalUser.get() : null;
		Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findByidPelangganOrNamaIgnoreCaseContainingOrAlamatIgnoreCaseContainingOrNoTelpOrUsers(paging,idPelanggan,nama,alamat,noTelp,users);
		List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
		List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
		return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
	}
	
//	public List<MasterPelangganWrapper> findByNama(String nama,Long idPelanggan,String alamat,Long noTelp,Long userId){
//		Optional<Users> optionalUser = usersRepository.findById(userId);
//		Users users = optionalUser.isPresent() ? optionalUser.get() : null;
//		List<MasterPelanggan> masterPelangganList = masterPelangganRepository.findByidPelangganOrNamaIgnoreCaseContainingOrAlamatIgnoreCaseContainingOrNoTelpOrUsers(idPelanggan,nama,alamat,noTelp,users);
//		return toWrapperList(masterPelangganList);
//	}
	
	
	
	 public ByteArrayInputStream load() {
		    List<MasterPelanggan> masterPelanggan = masterPelangganRepository.findAll();
		    ByteArrayInputStream in = ExcelHelperMasterPelanggan.tutorialsToExcel(masterPelanggan);
		    return in;
		  }
	 public List<MasterPelanggan> listAll() {
	        return masterPelangganRepository.findAll();
	    }
	
}

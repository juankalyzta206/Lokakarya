package com.ogya.lokakarya.telepon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.repository.HistoryRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.telepon.wrapper.MasterPelangganWrapper;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;


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
}

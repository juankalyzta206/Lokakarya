package com.ogya.lokakarya.usermanagement.service;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.HakAksesRepository;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;
import com.ogya.lokakarya.usermanagement.wrapper.UpdateUsersWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersLoginWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersWrapper;
import com.ogya.lokakarya.util.PaginationList;

@Service
@Transactional
public class UsersService {
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	HakAksesRepository hakAksesRepository;
	
	private String hashPassword(String plainPassword) {
        return new BCryptPasswordEncoder().encode(plainPassword);
    }
	
	private Boolean matchPassword(String plainPassword, String databasePassword) {
		return new BCryptPasswordEncoder().matches(plainPassword, databasePassword);
	}
	

	public List<UsersLoginWrapper> findByEmailOrUsernameAndPassword(String identity, String password) {
		if (usersRepository.isRegisteredEmail(identity) == 0) {
			if (usersRepository.isRegisteredUsername(identity) == 0) {
				throw new BusinessException("Email or Username is not Registered");
			} else {
				String databasePassword = usersRepository.hashedPasswordUsername(identity);
				if (!matchPassword(password, databasePassword)) {
					throw new BusinessException("Wrong Password");
				} else {
					List<Users> loginList = usersRepository.findByUsernameAndPassword(identity, databasePassword);
					return toWrapperListLogin(loginList);
				}
			}
		} else {
			String databasePassword = usersRepository.hashedPasswordEmail(identity);
			if (!matchPassword(password, databasePassword)) {
				throw new BusinessException("Wrong Password");
			} else {
				List<Users> loginList = usersRepository.findByEmailAndPassword(identity, databasePassword);
				return toWrapperListLogin(loginList);
			}
		}
	}
	
	public PaginationList<UsersWrapper, Users> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Users> usersPage = usersRepository.findAll(paging);
		List<Users> usersList = usersPage.getContent();
		List<UsersWrapper> usersWrapperList = toWrapperList(usersList);
		return new PaginationList<UsersWrapper, Users>(usersWrapperList, usersPage);
	}
	
	
	public List<UsersWrapper> findByEmailAndPassword(String email, String password) {
		List<Users> loginList = usersRepository.findByEmailAndPassword(email, password);
		return toWrapperList(loginList);
	}
	
	

	private UsersWrapper toWrapper(Users entity) {
		UsersWrapper wrapper = new UsersWrapper();
		wrapper.setUserId(entity.getUserId());
		wrapper.setUsername(entity.getUsername());
		wrapper.setPassword(entity.getPassword());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setEmail(entity.getEmail());
		wrapper.setTelp(entity.getTelp());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}
	
	private UsersLoginWrapper toWrapperLogin(Users entity) {
		UsersLoginWrapper wrapper = new UsersLoginWrapper();
		wrapper.setUserId(entity.getUserId());
		wrapper.setUsername(entity.getUsername());
		wrapper.setPassword(entity.getPassword());
		wrapper.setHakAkses(entity.getHakAkses());		
		wrapper.setNama(entity.getNama());
		return wrapper;
	}
	

	private List<UsersWrapper> toWrapperList(List<Users> entityList) {
		List<UsersWrapper> wrapperList = new ArrayList<UsersWrapper>();
		for (Users entity : entityList) {
			UsersWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	private List<UsersLoginWrapper> toWrapperListLogin(List<Users> entityList) {
		List<UsersLoginWrapper> wrapperList = new ArrayList<UsersLoginWrapper>();
		for (Users entity : entityList) {
			UsersLoginWrapper wrapper = toWrapperLogin(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public List<UsersWrapper> findAll() {
		List<Users> userList = usersRepository.findAll(Sort.by(Order.by("userId")).ascending());
		return toWrapperList(userList);
	}
	
	public List<UsersWrapper> findByUserId(Long userId) {
		List<Users> userList = usersRepository.findByUserId(userId);
		return toWrapperList(userList);
	}

	private Users toEntity(UsersWrapper wrapper) {
		Users entity = new Users();
		if (wrapper.getUserId() != null) {
			entity = usersRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setUsername(wrapper.getUsername());
		entity.setPassword(wrapper.getPassword());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setEmail(wrapper.getEmail());
		entity.setTelp(wrapper.getTelp());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}
	
	private Users toEntityUpdate(UpdateUsersWrapper wrapper) {
		Users entity = new Users();
		if (wrapper.getUserId() != null) {
			entity = usersRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setUsername(wrapper.getUsername());
		entity.setPassword(wrapper.getPassword());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setEmail(wrapper.getEmail());
		entity.setTelp(wrapper.getTelp());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public UsersWrapper save(UsersWrapper wrapper) {
		if (usersRepository.checkUsername(wrapper.getUsername())==0) {
			if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
				wrapper.setPassword(hashPassword(wrapper.getPassword()));
				Users user = usersRepository.save(toEntity(wrapper));
				return toWrapper(user);
			} else {
				throw new BusinessException("Email already taken");
			}
		} else {
			throw new BusinessException("Username already taken");
		}
	}
	
	public UsersWrapper update(UpdateUsersWrapper wrapper) {
		if (wrapper.getSameUsername() == 0) {
			if (wrapper.getSameEmail() == 0) {
				if (usersRepository.checkUsername(wrapper.getUsername())==0) {
					if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
						wrapper.setPassword(hashPassword(wrapper.getPassword()));
						Users user = usersRepository.save(toEntityUpdate(wrapper));
						return toWrapper(user);
					} else {
						throw new BusinessException("Email already taken");
					}
				} else if (usersRepository.checkUsername(wrapper.getUsername())==1){
					if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
						throw new BusinessException("Username already taken");
					}
				}
				else {
					throw new BusinessException("Username and email already taken");
				}
			}
			if (wrapper.getSameEmail() == 1) {
				if (usersRepository.checkUsername(wrapper.getUsername()) == 0) {
					wrapper.setPassword(hashPassword(wrapper.getPassword()));
					Users user = usersRepository.save(toEntityUpdate(wrapper));
					return toWrapper(user);
				} else {
					throw new BusinessException("Username already taken");
				}
			}
		} else {
			if (wrapper.getSameEmail() == 0) {
				if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
					wrapper.setPassword(hashPassword(wrapper.getPassword()));
					Users user = usersRepository.save(toEntityUpdate(wrapper));
					return toWrapper(user);
				} else {
					throw new BusinessException("Email already taken");
				}
			}
			if (wrapper.getSameEmail() == 1) {
				wrapper.setPassword(hashPassword(wrapper.getPassword()));
				Users user = usersRepository.save(toEntityUpdate(wrapper));
				return toWrapper(user);
			}
		}
		throw new BusinessException("Wrong input");
	}

	public void delete(Long id) {
		if (usersRepository.isExistHakAkses(id) == 0) {
			usersRepository.deleteById(id);
		} else {
			throw new BusinessException("User ID cannot deleted. User ID is still used in the HAK_AKSES table");
		}
		
	}
}
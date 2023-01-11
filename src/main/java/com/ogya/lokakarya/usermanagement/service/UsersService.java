package com.ogya.lokakarya.usermanagement.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.HakAksesRepository;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersRegisterWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersUpdateWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.login.UsersLoginWrapper;
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
	
	private UsersRegisterWrapper toWrapperRegister(Users entity) {
		UsersRegisterWrapper wrapper = new UsersRegisterWrapper();
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
	
	private UsersAddWrapper toWrapperAdd(Users entity) {
		UsersAddWrapper wrapper = new UsersAddWrapper();
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
		wrapper.setUsername(entity.getUsername());
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
	
	private Users toEntityUpdate(UsersUpdateWrapper wrapper) {
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
	
	private Users toEntityRegister(UsersRegisterWrapper wrapper) {
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
	
	private Users toEntityAdd(UsersAddWrapper wrapper) {
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

	public UsersRegisterWrapper register(UsersRegisterWrapper wrapper) {
		if (usersRepository.checkUsername(wrapper.getUsername())==0) {
			if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
				wrapper.setPassword(hashPassword(wrapper.getPassword()));
				Users user = usersRepository.save(toEntityRegister(wrapper));
				return toWrapperRegister(user);
			} else {
				throw new BusinessException("Email already taken");
			}
		} else {
			throw new BusinessException("Username already taken");
		}
	}
	
	
	public UsersAddWrapper save(UsersAddWrapper wrapper) {
		if (usersRepository.checkUsername(wrapper.getUsername())==0) {
			if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
				wrapper.setPassword(hashPassword(wrapper.getPassword()));
				Users user = usersRepository.save(toEntityAdd(wrapper));
				return toWrapperAdd(user);
			} else {
				throw new BusinessException("Email already taken");
			}
		} else {
			throw new BusinessException("Username already taken");
		}
	}
	
	public UsersWrapper update(UsersUpdateWrapper wrapper) {
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
	
	public void ExportToPdf(HttpServletResponse response) throws Exception{
		 // Call the findAll method to retrieve the data
	    List<Users> data = usersRepository.findAll();
	    
	    // Now create a new iText PDF document
	    Document pdfDoc = new Document(PageSize.A4.rotate());
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
	    pdfDoc.open();
	    
	    Paragraph title = new Paragraph("List Users",
	            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);
	    
	    // Add the generation date
	    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

	    // Create a table
	    PdfPTable pdfTable = new PdfPTable(10); 
	    

	    pdfTable.setWidthPercentage(100);
	    pdfTable.setSpacingBefore(10f);
	    pdfTable.setSpacingAfter(10f);
	         
	  
	        pdfTable.addCell("Username");
	        pdfTable.addCell("Nama");
	        pdfTable.addCell("Alamat");
	        pdfTable.addCell("Email");
	        pdfTable.addCell("Telp");
	        pdfTable.addCell("Program Name");
	        pdfTable.addCell("Created Date");
	        pdfTable.addCell("Created By");
	        pdfTable.addCell("Updated Date");
	        pdfTable.addCell("Updated By");  
	        BaseColor color = new BaseColor(135,206,235);
	    	for(int i=0;i<10;i++) {
	    		pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
	    	}
	    
	    // Iterate through the data and add it to the table
	    for (Users entity : data) {
	    	pdfTable.addCell(String.valueOf(entity.getUsername() != null ? String.valueOf(entity.getUsername()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getEmail() != null ? String.valueOf(entity.getEmail()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getTelp() != null ? String.valueOf(entity.getTelp()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getProgramName() != null ? String.valueOf(entity.getProgramName()) : "-"));
	    	
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    	String createdDate = "-";
	    	if (entity.getCreatedDate() != null) {
	    		createdDate = formatter.format(entity.getCreatedDate());
	    	}
	    	pdfTable.addCell(createdDate);
	    	pdfTable.addCell(String.valueOf(entity.getCreatedBy() != null ? String.valueOf(entity.getCreatedBy()) : "-"));
	    	
	    	String updatedDate = "-";
	    	if (entity.getUpdatedDate() != null) {
	    		updatedDate = formatter.format(entity.getUpdatedDate());
		    	}
	    	pdfTable.addCell(updatedDate);
	    	pdfTable.addCell(String.valueOf(entity.getUpdatedBy() != null ? String.valueOf(entity.getUpdatedBy()) : "-"));
	    	
	    }
	    
	    // Add the table to the pdf document
	    pdfDoc.add(pdfTable);

	    pdfDoc.close();
	    pdfWriter.close();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}
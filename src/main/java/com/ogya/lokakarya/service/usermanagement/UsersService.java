/*
* UsersService.java
*	This class is provide service relate to users table such as
*	CRUD, login, register, pagination, and export to PDF
*
* Version 1.0
*
* Copyright : Irzan Maulana, Backend Team OGYA
*/

package com.ogya.lokakarya.service.usermanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.configuration.usermanagement.UsersColumnProperties;
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.HakAksesRepository;
import com.ogya.lokakarya.repository.usermanagement.UsersRepository;
import com.ogya.lokakarya.repository.usermanagement.criteria.UsersCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.util.ExportData;
import com.ogya.lokakarya.wrapper.usermanagement.UsersAddWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.UsersRegisterWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.UsersUpdateWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.UsersWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.login.UsersLoginWrapper;

@Service
@Transactional
public class UsersService {
	@Autowired
	UsersRepository usersRepository;

	@Autowired
	HakAksesRepository hakAksesRepository;

	@Autowired
	UsersCriteriaRepository usersCriteriaRepository;

	@Autowired
	UsersColumnProperties usersColumnProperties;

	public PaginationList<UsersWrapper, Users> ListWithPaging(PagingRequestWrapper request) {
		/* query users table with pagination */
		List<Users> usersList = usersCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage()) * (request.getSize());
		int toIndex = Math.min(fromIndex + request.getSize(), usersList.size());
		Page<Users> usersPage = new PageImpl<>(usersList.subList(fromIndex, toIndex),
				PageRequest.of(request.getPage(), request.getSize()), usersList.size());
		List<UsersWrapper> usersWrapperList = new ArrayList<>();
		for (Users entity : usersPage) {
			usersWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<UsersWrapper, Users>(usersWrapperList, usersPage);
	}

	private String hashPassword(String plainPassword) {
		/* hashing password with BCrypt */
		return new BCryptPasswordEncoder().encode(plainPassword);
	}

	private Boolean matchPassword(String plainPassword, String databasePassword) {
		/* matching password for login with database hashed password */
		return new BCryptPasswordEncoder().matches(plainPassword, databasePassword);
	}

	public List<UsersLoginWrapper> findByEmailOrUsernameAndPassword(String identity, String password) {
		/* login service, matching by username/email with password */
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
		/* query all users table with pagination */
		Pageable paging = PageRequest.of(page, size, Sort.by("userId").ascending());
		Page<Users> usersPage = usersRepository.findAll(paging);
		List<Users> usersList = usersPage.getContent();
		List<UsersWrapper> usersWrapperList = toWrapperList(usersList);
		return new PaginationList<UsersWrapper, Users>(usersWrapperList, usersPage);
	}

	private UsersWrapper toWrapper(Users entity) {
		/* set value wrapper/response based other service result */
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
		/* set value wrapper/response register based other service result */
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
		/* set value wrapper/response add users based other service result */
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
		/* set value wrapper/response login based other service result */
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
		/* find all users service */
		List<Users> userList = usersRepository.findAll(Sort.by(Order.by("userId")).ascending());
		return toWrapperList(userList);
	}

	public List<UsersWrapper> findByUserId(Long userId) {
		/* find user by user ID */
		List<Users> userList = usersRepository.findByUserId(userId);
		return toWrapperList(userList);
	}

	private Users toEntityUpdate(UsersUpdateWrapper wrapper) {
		/* update users data to users table */
		Users entity = new Users();
		if (wrapper.getUserId() != null) {
			entity = usersRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setUsername(wrapper.getUsername());
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
		/* register users data to users table */
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
		/* add users data to users table */
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
		/* register service, check if already exist username/email */
		if (usersRepository.checkUsername(wrapper.getUsername()) == 0) {
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
		/* add service, check if already exist username/email */
		if (usersRepository.checkUsername(wrapper.getUsername()) == 0) {
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
		/*
		 * for update user, check if input username or email same as old data, and check
		 * if username or email already exist at database or not
		 */
		if (wrapper.getSameUsername() == 0) {
			if (wrapper.getSameEmail() == 0) {
				if (usersRepository.checkUsername(wrapper.getUsername()) == 0) {
					if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
						Users user = usersRepository.save(toEntityUpdate(wrapper));
						return toWrapper(user);
					} else {
						throw new BusinessException("Email already taken");
					}
				} else if (usersRepository.checkUsername(wrapper.getUsername()) == 1) {
					if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
						throw new BusinessException("Username already taken");
					}
				} else {
					throw new BusinessException("Username and email already taken");
				}
			}
			if (wrapper.getSameEmail() == 1) {
				if (usersRepository.checkUsername(wrapper.getUsername()) == 0) {
					Users user = usersRepository.save(toEntityUpdate(wrapper));
					return toWrapper(user);
				} else {
					throw new BusinessException("Username already taken");
				}
			}
		} else {
			if (wrapper.getSameEmail() == 0) {
				if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
					Users user = usersRepository.save(toEntityUpdate(wrapper));
					return toWrapper(user);
				} else {
					throw new BusinessException("Email already taken");
				}
			}
			if (wrapper.getSameEmail() == 1) {
				Users user = usersRepository.save(toEntityUpdate(wrapper));
				return toWrapper(user);
			}
		}
		throw new BusinessException("Wrong input");
	}

	public void delete(Long id) {
		/*
		 * delete users based user ID check if user exist at HakAkses table, if not
		 * existed then delete
		 */
		if (usersRepository.isExistHakAkses(id) == 0) {

			usersRepository.deleteById(id);
		} else {
			throw new BusinessException("User ID cannot deleted. User ID is still used in the HAK_AKSES table");
		}

	}

	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}

	public void ExportToPdf(HttpServletResponse response) throws Exception {
		/* Call the findAll method to retrieve the data */
		List<Users> data = usersRepository.findAll(Sort.by(Order.by("userId")).ascending());

		List<String> columnNames = usersColumnProperties.getColumn();
		int columnLength = columnNames.size();

		/* Create a new iText PDF document */
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("List Users", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		/* Add the generation date */
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		/* Create a pdf table */
		PdfPTable pdfTable = new PdfPTable(columnLength);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		for (String columnName : columnNames) {
			pdfTable.addCell(Align(columnName));
		}
		BaseColor color = new BaseColor(135, 206, 235);
		for (int i = 0; i < columnLength; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		/* Iterate through the data and add it to the table */
		ExportData<Users> parsing = new ExportData<Users>();
		pdfTable = parsing.exportPdf(columnNames, data, pdfTable);

		/* Add the table to the pdf document */
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}

}

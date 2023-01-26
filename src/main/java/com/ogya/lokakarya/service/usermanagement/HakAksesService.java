package com.ogya.lokakarya.service.usermanagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
import com.ogya.lokakarya.configuration.usermanagement.HakAksesColumnProperties;
import com.ogya.lokakarya.entity.usermanagement.HakAkses;
import com.ogya.lokakarya.entity.usermanagement.Roles;
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.HakAksesRepository;
import com.ogya.lokakarya.repository.usermanagement.RolesRepository;
import com.ogya.lokakarya.repository.usermanagement.UsersRepository;
import com.ogya.lokakarya.repository.usermanagement.criteria.HakAksesCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.HakAksesWrapper;

@Service
@Transactional
public class HakAksesService {
	@Autowired
	HakAksesRepository hakAksesRepository;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	HakAksesCriteriaRepository hakAksesCriteriaRepository;
	
	@Autowired
	HakAksesColumnProperties hakAksesColumnProperties;

	public PaginationList<HakAksesWrapper, HakAkses> ListWithPaging(PagingRequestWrapper request) {
		List<HakAkses> hakAksesList = hakAksesCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage()) * request.getSize();
		int toIndex = Math.min(fromIndex + request.getSize(), hakAksesList.size());
		Page<HakAkses> hakAksesPage = new PageImpl<>(hakAksesList.subList(fromIndex, toIndex),
				PageRequest.of(request.getPage(), request.getSize()), hakAksesList.size());
		List<HakAksesWrapper> hakAksesWrapperList = new ArrayList<>();
		for (HakAkses entity : hakAksesPage) {
			hakAksesWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<HakAksesWrapper, HakAkses>(hakAksesWrapperList, hakAksesPage);
	}

	public PaginationList<HakAksesWrapper, HakAkses> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<HakAkses> hakAksesPage = hakAksesRepository.findAll(paging);
		List<HakAkses> hakAksesList = hakAksesPage.getContent();
		List<HakAksesWrapper> hakAksesWrapperList = toWrapperList(hakAksesList);
		return new PaginationList<HakAksesWrapper, HakAkses>(hakAksesWrapperList, hakAksesPage);
	}

	private HakAksesWrapper toWrapper(HakAkses entity) {
		HakAksesWrapper wrapper = new HakAksesWrapper();
		wrapper.setHakAksesId(entity.getHakAksesId());
		wrapper.setUserId(entity.getUsers() != null ? entity.getUsers().getUserId() : null);
		wrapper.setRoleId(entity.getRoles() != null ? entity.getRoles().getRoleId() : null);
		wrapper.setUsername(entity.getUsers() != null ? entity.getUsers().getUsername() : null);
		wrapper.setRoleName(entity.getRoles() != null ? entity.getRoles().getNama() : null);
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}

	private List<HakAksesWrapper> toWrapperList(List<HakAkses> entityList) {
		List<HakAksesWrapper> wrapperList = new ArrayList<HakAksesWrapper>();
		for (HakAkses entity : entityList) {
			HakAksesWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<HakAksesWrapper> findAll() {
		List<HakAkses> hakAksesList = hakAksesRepository.findAll(Sort.by(Order.by("hakAksesId")).ascending());
		return toWrapperList(hakAksesList);
	}

	private HakAkses toEntity(HakAksesWrapper wrapper) {
		HakAkses entity = new HakAkses();
		if (wrapper.getHakAksesId() != null) {
			entity = hakAksesRepository.getReferenceById(wrapper.getHakAksesId());
		}
		Optional<Users> optionalUsers = usersRepository.findById(wrapper.getUserId());
		Users users = optionalUsers.isPresent() ? optionalUsers.get() : null;
		entity.setUsers(users);
		Optional<Roles> optionalRoles = rolesRepository.findById(wrapper.getRoleId());
		Roles roles = optionalRoles.isPresent() ? optionalRoles.get() : null;
		entity.setRoles(roles);
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public HakAksesWrapper save(HakAksesWrapper wrapper) {
		if (hakAksesRepository.isExistHakAkses(wrapper.getUserId(), wrapper.getRoleId()) > 0) {
			throw new BusinessException("Cannot add hak akses, same User ID and Role ID already exist");
		}
		if (hakAksesRepository.isExistUser(wrapper.getUserId()) == 0) {
			throw new BusinessException("Cannot add hak akses, User ID not exist");
		}
		if (hakAksesRepository.isExistRole(wrapper.getRoleId()) == 0) {
			throw new BusinessException("Cannot add hak akses, Role ID not exist");
		}
		HakAkses hakAkses = hakAksesRepository.save(toEntity(wrapper));
		return toWrapper(hakAkses);
	}

	public void delete(Long id) {
		hakAksesRepository.deleteById(id);
	}
	
	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	
	public boolean containsChar(String s, char search) {
	    if (s.length() == 0)
	        return false;
	    else
	        return s.charAt(0) == search || containsChar(s.substring(1), search);
	}

	public void ExportToPdf(HttpServletResponse response) throws Exception {
		/* Call the findAll method to retrieve the data */
		List<HakAkses> data = hakAksesRepository.findAll(Sort.by(Order.by("hakAksesId")).ascending());

		List<String> columnNames = hakAksesColumnProperties.getColumn();
		int columnLength = columnNames.size();

		/* Create a new iText PDF document */
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("List Hak Akses", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
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
		for (HakAkses entity : data) {
			for (String columnName : columnNames) {
				String value = "-";
				try {
					String columnNameNoSpace = columnName.replaceAll("\\s", "");
					Boolean isForeignKey = containsChar(columnNameNoSpace,':');
					String[] foreignClass = columnNameNoSpace.split(":", 2);
					if (!isForeignKey) {
						Method method = HakAkses.class.getMethod(
								"get" + columnNameNoSpace);
						Object result = method.invoke(entity);
						value = result != null ? result.toString() : "-";
					} else {
						Method method = HakAkses.class.getMethod(
								"get" + foreignClass[0]);
						if (foreignClass[0].equals("Users")) {
							Method usersMethod = Users.class.getMethod(
		                            "get" + foreignClass[1]);
							Object result = usersMethod.invoke(method.invoke(entity));
							value = result != null ? result.toString() : "-";
						} else if (foreignClass[0].equals("Roles")) {
							Method rolesMethod = Roles.class.getMethod(
		                            "get" + foreignClass[1]);
							Object result = rolesMethod.invoke(method.invoke(entity));
							value = result != null ? result.toString() : "-";
						}	
						
					}
					
					
					
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					/* Handle the exception if the method is not found or cannot be invoked */
				}
				pdfTable.addCell(Align(value));
			}
		}

		/* Add the table to the pdf document */
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}

}
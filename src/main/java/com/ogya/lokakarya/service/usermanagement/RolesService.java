/*
* RolesService.java
*	This class is provide service relate to roles table such as
*	CRUD, pagination, and export to PDF
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
import com.ogya.lokakarya.configuration.usermanagement.RolesColumnProperties;
import com.ogya.lokakarya.entity.usermanagement.Roles;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.RolesRepository;
import com.ogya.lokakarya.repository.usermanagement.criteria.RolesCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.util.ParsingColumn;
import com.ogya.lokakarya.wrapper.usermanagement.RolesWrapper;

@Service
@Transactional
public class RolesService {
	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	RolesCriteriaRepository rolesCriteriaRepository;
	
	@Autowired
	RolesColumnProperties rolesColumnProperties;

	public PaginationList<RolesWrapper, Roles> ListWithPaging(PagingRequestWrapper request) {
		List<Roles> rolesList = rolesCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage()) * request.getSize();
		int toIndex = Math.min(fromIndex + request.getSize(), rolesList.size());
		Page<Roles> rolesPage = new PageImpl<>(rolesList.subList(fromIndex, toIndex),
				PageRequest.of(request.getPage(), request.getSize()), rolesList.size());
		List<RolesWrapper> rolesWrapperList = new ArrayList<>();
		for (Roles entity : rolesPage) {
			rolesWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<RolesWrapper, Roles>(rolesWrapperList, rolesPage);
	}

	public PaginationList<RolesWrapper, Roles> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Roles> rolesPage = rolesRepository.findAll(paging);
		List<Roles> rolesList = rolesPage.getContent();
		List<RolesWrapper> rolesWrapperList = toWrapperList(rolesList);
		return new PaginationList<RolesWrapper, Roles>(rolesWrapperList, rolesPage);
	}

	private RolesWrapper toWrapper(Roles entity) {
		RolesWrapper wrapper = new RolesWrapper();
		wrapper.setRoleId(entity.getRoleId());
		wrapper.setNama(entity.getNama());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}

	private List<RolesWrapper> toWrapperList(List<Roles> entityList) {
		List<RolesWrapper> wrapperList = new ArrayList<RolesWrapper>();
		for (Roles entity : entityList) {
			RolesWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<RolesWrapper> findAll() {
		List<Roles> rolesList = rolesRepository.findAll(Sort.by(Order.by("roleId")).ascending());
		return toWrapperList(rolesList);
	}

	private Roles toEntity(RolesWrapper wrapper) {
		Roles entity = new Roles();
		if (wrapper.getRoleId() != null) {
			entity = rolesRepository.getReferenceById(wrapper.getRoleId());
		}
		entity.setNama(wrapper.getNama());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public RolesWrapper save(RolesWrapper wrapper) {
		if (rolesRepository.isExistRoleName(wrapper.getNama()) == 0) {
			Roles roles = rolesRepository.save(toEntity(wrapper));
			return toWrapper(roles);
		} else {
			throw new BusinessException("Role name already taken");
		}
	}

	public void delete(Long id) {
		if (rolesRepository.isExistHakAkses(id) == 0) {
			if (rolesRepository.isExistRoleMenu(id) == 0) {
				rolesRepository.deleteById(id);
			} else {
				throw new BusinessException("Role ID cannot deleted. Role ID is still used in the ROLE_MENU table");
			}
		} else {
			throw new BusinessException("Role ID cannot deleted. Role ID is still used in the HAK_AKSES table");
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
		List<Roles> data = rolesRepository.findAll(Sort.by(Order.by("roleId")).ascending());

		List<String> columnNames = rolesColumnProperties.getColumn();
		int columnLength = columnNames.size();

		/* Create a new iText PDF document */
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("List Roles", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
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
		ParsingColumn<Roles> parsing = new ParsingColumn<Roles>();
		pdfTable = parsing.inputPdf(columnNames, data, pdfTable);
		

		/* Add the table to the pdf document */
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}
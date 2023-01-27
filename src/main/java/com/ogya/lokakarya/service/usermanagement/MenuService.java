/*
* MenuService.java
*	This class is provide service relate to menu table such as
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
import com.ogya.lokakarya.configuration.usermanagement.MenuColumnProperties;
import com.ogya.lokakarya.entity.usermanagement.Menu;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.MenuRepository;
import com.ogya.lokakarya.repository.usermanagement.criteria.MenuCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.util.ParsingColumn;
import com.ogya.lokakarya.wrapper.usermanagement.MenuWrapper;

@Service
@Transactional
public class MenuService {
	@Autowired
	MenuRepository menuRepository;

	@Autowired
	MenuCriteriaRepository menuCriteriaRepository;
	
	@Autowired
	MenuColumnProperties menuColumnProperties;

	public PaginationList<MenuWrapper, Menu> ListWithPaging(PagingRequestWrapper request) {
		List<Menu> menuList = menuCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage()) * request.getSize();
		int toIndex = Math.min(fromIndex + request.getSize(), menuList.size());
		Page<Menu> menuPage = new PageImpl<>(menuList.subList(fromIndex, toIndex),
				PageRequest.of(request.getPage(), request.getSize()), menuList.size());
		List<MenuWrapper> menuWrapperList = new ArrayList<>();
		for (Menu entity : menuPage) {
			menuWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<MenuWrapper, Menu>(menuWrapperList, menuPage);
	}

	public PaginationList<MenuWrapper, Menu> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Menu> menuPage = menuRepository.findAll(paging);
		List<Menu> menuList = menuPage.getContent();
		List<MenuWrapper> menuWrapperList = toWrapperList(menuList);
		return new PaginationList<MenuWrapper, Menu>(menuWrapperList, menuPage);
	}

	private MenuWrapper toWrapper(Menu entity) {
		MenuWrapper wrapper = new MenuWrapper();
		wrapper.setMenuId(entity.getMenuId());
		wrapper.setNama(entity.getNama());
		wrapper.setIcon(entity.getIcon());
		wrapper.setUrl(entity.getUrl());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}

	private List<MenuWrapper> toWrapperList(List<Menu> entityList) {
		List<MenuWrapper> wrapperList = new ArrayList<MenuWrapper>();
		for (Menu entity : entityList) {
			MenuWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<MenuWrapper> findAll() {
		List<Menu> menulist = menuRepository.findAll(Sort.by(Order.by("menuId")).ascending());
		return toWrapperList(menulist);
	}

	private Menu toEntity(MenuWrapper wrapper) {
		Menu entity = new Menu();
		if (wrapper.getMenuId() != null) {
			entity = menuRepository.getReferenceById(wrapper.getMenuId());
		}
		entity.setNama(wrapper.getNama());
		entity.setIcon(wrapper.getIcon());
		entity.setUrl(wrapper.getUrl());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public MenuWrapper save(MenuWrapper wrapper) {
		Menu menu = menuRepository.save(toEntity(wrapper));
		return toWrapper(menu);
	}

	public void delete(Long id) {
		if (menuRepository.isExistRoleMenu(id) == 0) {
			menuRepository.deleteById(id);
		} else {
			throw new BusinessException("Menu ID cannot deleted. Menu ID is still used in the ROLE_MENU table");
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
		List<Menu> data = menuRepository.findAll(Sort.by(Order.by("menuId")).ascending());

		List<String> columnNames = menuColumnProperties.getColumn();
		int columnLength = columnNames.size();

		/* Create a new iText PDF document */
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("List Menu", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
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
    	String path = "com.ogya.lokakarya.entity.usermanagement.";
		ParsingColumn<Menu> parsing = new ParsingColumn<Menu>();
		pdfTable = parsing.ParsePdf(columnNames, data, pdfTable, path);

		/* Add the table to the pdf document */
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}
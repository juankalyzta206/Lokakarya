/*
* SubMenuService.java
*	This class is provide service relate to sub menu table such as
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
import com.ogya.lokakarya.configuration.usermanagement.SubMenuColumnProperties;
import com.ogya.lokakarya.entity.usermanagement.Menu;
import com.ogya.lokakarya.entity.usermanagement.SubMenu;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.MenuRepository;
import com.ogya.lokakarya.repository.usermanagement.SubMenuRepository;
import com.ogya.lokakarya.repository.usermanagement.criteria.SubMenuCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.util.ExportData;
import com.ogya.lokakarya.wrapper.usermanagement.SubMenuWrapper;

@Service
@Transactional
public class SubMenuService {
	@Autowired
	SubMenuRepository subMenuRepository;

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	SubMenuCriteriaRepository subMenuCriteriaRepository;
	
	@Autowired
	SubMenuColumnProperties subMenuColumnProperties;

	public PaginationList<SubMenuWrapper, SubMenu> ListWithPaging(PagingRequestWrapper request) {
		List<SubMenu> subMenuList = subMenuCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage()) * request.getSize();
		int toIndex = Math.min(fromIndex + request.getSize(), subMenuList.size());
		Page<SubMenu> subMenuPage = new PageImpl<>(subMenuList.subList(fromIndex, toIndex),
				PageRequest.of(request.getPage(), request.getSize()), subMenuList.size());
		List<SubMenuWrapper> subMenuWrapperList = new ArrayList<>();
		for (SubMenu entity : subMenuPage) {
			subMenuWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<SubMenuWrapper, SubMenu>(subMenuWrapperList, subMenuPage);
	}

	public PaginationList<SubMenuWrapper, SubMenu> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<SubMenu> subMenuPage = subMenuRepository.findAll(paging);
		List<SubMenu> subMenuList = subMenuPage.getContent();
		List<SubMenuWrapper> subMenuWrapperList = toWrapperList(subMenuList);
		return new PaginationList<SubMenuWrapper, SubMenu>(subMenuWrapperList, subMenuPage);
	}

	private SubMenuWrapper toWrapper(SubMenu entity) {
		SubMenuWrapper wrapper = new SubMenuWrapper();
		wrapper.setSubMenuId(entity.getSubMenuId());
		wrapper.setMenuId(entity.getMenu() != null ? entity.getMenu().getMenuId() : null);
		wrapper.setMenuName(entity.getMenu() != null ? entity.getMenu().getNama() : null);
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

	private List<SubMenuWrapper> toWrapperList(List<SubMenu> entityList) {
		List<SubMenuWrapper> wrapperList = new ArrayList<SubMenuWrapper>();
		for (SubMenu entity : entityList) {
			SubMenuWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public List<SubMenuWrapper> findAll() {
		List<SubMenu> subMenuList = subMenuRepository.findAll(Sort.by(Order.by("subMenuId")).ascending());
		return toWrapperList(subMenuList);
	}

	private SubMenu toEntity(SubMenuWrapper wrapper) {
		SubMenu entity = new SubMenu();
		if (wrapper.getSubMenuId() != null) {
			entity = subMenuRepository.getReferenceById(wrapper.getSubMenuId());
		}
		Optional<Menu> optionalMenu = menuRepository.findById(wrapper.getMenuId());
		Menu menu = optionalMenu.isPresent() ? optionalMenu.get() : null;
		entity.setMenu(menu);
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

	public SubMenuWrapper save(SubMenuWrapper wrapper) {
		if (subMenuRepository.isExistMenu(wrapper.getMenuId()) == 0) {
			throw new BusinessException("Cannot add role menu, Menu ID not exist");
		}
		SubMenu subMenu = subMenuRepository.save(toEntity(wrapper));
		return toWrapper(subMenu);
	}

	public void delete(Long id) {
		subMenuRepository.deleteById(id);
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
		List<SubMenu> data = subMenuRepository.findAll(Sort.by(Order.by("subMenuId")).ascending());

		List<String> columnNames = subMenuColumnProperties.getColumn();
		int columnLength = columnNames.size();

		/* Create a new iText PDF document */
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("List Sub Menu", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		/* Add the generation date */
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		/* Create a pdf table */
		PdfPTable pdfTable = new PdfPTable(columnLength);

		/* Iterate through the data and add it to the table */
		ExportData<SubMenu> parsing = new ExportData<SubMenu>();
		pdfTable = parsing.exportPdf(columnNames, data, pdfTable);

		/* Add the table to the pdf document */
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}
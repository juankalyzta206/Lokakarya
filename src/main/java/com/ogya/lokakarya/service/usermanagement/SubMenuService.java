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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.entity.usermanagement.Menu;
import com.ogya.lokakarya.entity.usermanagement.SubMenu;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.MenuRepository;
import com.ogya.lokakarya.repository.usermanagement.SubMenuRepository;
import com.ogya.lokakarya.repository.usermanagement.criteria.SubMenuCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
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

	public void ExportToPdf(HttpServletResponse response) throws Exception {
		// Call the findAll method to retrieve the data
		List<SubMenu> data = subMenuRepository.findAll();

		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("List Users", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		// Add the generation date
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(8);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("Menu");
		pdfTable.addCell("Nama");
		pdfTable.addCell("Url");
		pdfTable.addCell("Program Name");
		pdfTable.addCell("Created Date");
		pdfTable.addCell("Created By");
		pdfTable.addCell("Updated Date");
		pdfTable.addCell("Updated By");
		BaseColor color = new BaseColor(135, 206, 235);
		for (int i = 0; i < 8; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		// Iterate through the data and add it to the table
		for (SubMenu entity : data) {
			pdfTable.addCell(String
					.valueOf(entity.getMenu().getNama() != null ? String.valueOf(entity.getMenu().getNama()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getUrl() != null ? String.valueOf(entity.getUrl()) : "-"));
			pdfTable.addCell(
					String.valueOf(entity.getProgramName() != null ? String.valueOf(entity.getProgramName()) : "-"));

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String createdDate = "-";
			if (entity.getCreatedDate() != null) {
				createdDate = formatter.format(entity.getCreatedDate());
			}
			pdfTable.addCell(createdDate);
			pdfTable.addCell(
					String.valueOf(entity.getCreatedBy() != null ? String.valueOf(entity.getCreatedBy()) : "-"));

			String updatedDate = "-";
			if (entity.getUpdatedDate() != null) {
				updatedDate = formatter.format(entity.getUpdatedDate());
			}
			pdfTable.addCell(updatedDate);
			pdfTable.addCell(
					String.valueOf(entity.getUpdatedBy() != null ? String.valueOf(entity.getUpdatedBy()) : "-"));

		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}
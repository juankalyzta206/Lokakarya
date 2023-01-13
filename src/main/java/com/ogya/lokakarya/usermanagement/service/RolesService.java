package com.ogya.lokakarya.usermanagement.service;

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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.usermanagement.repository.RolesRepository;
import com.ogya.lokakarya.usermanagement.repository.criteria.RolesCriteriaRepository;
import com.ogya.lokakarya.usermanagement.wrapper.RolesWrapper;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;

@Service
@Transactional
public class RolesService {
	@Autowired
	RolesRepository rolesRepository;
	
	@Autowired
	RolesCriteriaRepository rolesCriteriaRepository;

	public PaginationList<RolesWrapper, Roles> ListWithPaging(PagingRequestWrapper request) { 
		List<Roles> rolesList = rolesCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage())* request.getSize();
		int toIndex = Math.min(fromIndex + request.getSize(), rolesList.size());
		Page<Roles> rolesPage = new PageImpl<>(rolesList.subList(fromIndex, toIndex), PageRequest.of(request.getPage(), request.getSize()),rolesList.size());
		List<RolesWrapper> rolesWrapperList = new ArrayList<>();
		for(Roles entity : rolesPage) {
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
		Roles roles = rolesRepository.save(toEntity(wrapper));
		return toWrapper(roles);
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
	
	public void ExportToPdf(HttpServletResponse response) throws Exception{
		 // Call the findAll method to retrieve the data
	    List<Roles> data = rolesRepository.findAll();
	    
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
	    PdfPTable pdfTable = new PdfPTable(6); 
	    

	    pdfTable.setWidthPercentage(100);
	    pdfTable.setSpacingBefore(10f);
	    pdfTable.setSpacingAfter(10f);
	         
	  
	        pdfTable.addCell("Nama");
	        pdfTable.addCell("Program Name");
	        pdfTable.addCell("Created Date");
	        pdfTable.addCell("Created By");
	        pdfTable.addCell("Updated Date");
	        pdfTable.addCell("Updated By");  
	        BaseColor color = new BaseColor(135,206,235);
	    	for(int i=0;i<6;i++) {
	    		pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
	    	}
	    
	    // Iterate through the data and add it to the table
	    for (Roles entity : data) {
	    	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
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
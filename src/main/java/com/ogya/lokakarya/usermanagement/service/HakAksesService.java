package com.ogya.lokakarya.usermanagement.service;

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
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.HakAkses;
import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.HakAksesRepository;
import com.ogya.lokakarya.usermanagement.repository.RolesRepository;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;
import com.ogya.lokakarya.usermanagement.repository.criteria.HakAksesCriteriaRepository;
import com.ogya.lokakarya.usermanagement.wrapper.HakAksesWrapper;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;

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

	public PaginationList<HakAksesWrapper, HakAkses> ListWithPaging(PagingRequestWrapper request) { 
		List<HakAkses> hakAksesList = hakAksesCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage())* request.getSize();
		int toIndex = Math.min(fromIndex + request.getSize(), hakAksesList.size());
		Page<HakAkses> hakAksesPage = new PageImpl<>(hakAksesList.subList(fromIndex, toIndex), PageRequest.of(request.getPage(), request.getSize()),hakAksesList.size());
		List<HakAksesWrapper> hakAksesWrapperList = new ArrayList<>();
		for(HakAkses entity : hakAksesPage) {
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
		if (hakAksesRepository.isExistHakAkses(wrapper.getUserId(),wrapper.getRoleId()) > 0) {
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
	
	public void ExportToPdf(HttpServletResponse response) throws Exception{
		 // Call the findAll method to retrieve the data
	    List<HakAkses> data = hakAksesRepository.findAll();
	    
	    // Now create a new iText PDF document
	    Document pdfDoc = new Document(PageSize.A4.rotate());
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
	    pdfDoc.open();
	    
	    Paragraph title = new Paragraph("List Hak Akses",
	            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);
	    
	    // Add the generation date
	    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

	    // Create a table
	    PdfPTable pdfTable = new PdfPTable(7); 
	    

	    pdfTable.setWidthPercentage(100);
	    pdfTable.setSpacingBefore(10f);
	    pdfTable.setSpacingAfter(10f);
	         
	  
	        pdfTable.addCell("Username");
	        pdfTable.addCell("Role");
	        pdfTable.addCell("Program Name");
	        pdfTable.addCell("Created Date");
	        pdfTable.addCell("Created By");
	        pdfTable.addCell("Updated Date");
	        pdfTable.addCell("Updated By");  
	        BaseColor color = new BaseColor(135,206,235);
	    	for(int i=0;i<7;i++) {
	    		pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
	    	}
	    
	    // Iterate through the data and add it to the table
	    for (HakAkses entity : data) {
	    	pdfTable.addCell(String.valueOf(entity.getUsers().getUsername() != null ? String.valueOf(entity.getUsers().getUsername()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getRoles().getNama() != null ? String.valueOf(entity.getRoles().getNama()) : "-"));
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
package com.ogya.lokakarya.telepon.service;

import java.io.ByteArrayInputStream;
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
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.usermanagement.UsersRepository;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.helper.ExcelHelperMasterPelanggan;
import com.ogya.lokakarya.telepon.repository.HistoryRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.telepon.repository.criteria.MasterPelangganCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.telepon.MasterPelangganWrapper;
import com.ogya.lokakarya.wrapper.telepon.TeleponFilterWrapper;
import com.ogya.lokakarya.wrapper.telepon.TeleponPagingRequestWrapper;



@Service
@Transactional
public class MasterPelangganService {
	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	UsersRepository usersRepository;
	@Autowired
	MasterPelangganCriteriaRepository masterPelangganCriteriaRepository;
	
	//service untuk menampilkan semua list
	public List<MasterPelangganWrapper> findAll(){
		List<MasterPelanggan> masterPelangganList = masterPelangganRepository.findAll(Sort.by(Order.by("idPelanggan")).descending());
		return toWrapperList(masterPelangganList);
	}
	//service untuk memasukkan/mengubah entity 
	public MasterPelangganWrapper save(MasterPelangganWrapper wrapper) {
		MasterPelanggan masterPelanggan = masterPelangganRepository.save(toEntity(wrapper));
		return toWrapper(masterPelanggan);
	}
	//service untuk menghapus entity
	public void deleteById(Long masterPelangganId) {
		if( transaksiTelkomRepository.findById(masterPelangganId).isPresent()) {
			throw new BusinessException("id tidak bisa dihapus");
		}
		else {
			masterPelangganRepository.deleteById(masterPelangganId);
			
		}
	}
	//method dalam service untuk mengubah entity ke wrapper
	private MasterPelangganWrapper toWrapper(MasterPelanggan entity) {
		MasterPelangganWrapper wrapper = new MasterPelangganWrapper();
		wrapper.setIdPelanggan(entity.getIdPelanggan());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setNama(entity.getNama());
		wrapper.setNoTelp(entity.getNoTelp());
		wrapper.setUserId(entity.getUsers() != null ? entity.getUsers().getUserId() : null);
		return wrapper;
	}
	//method dalam service untuk memasukkan nilai kedalam entity
	private MasterPelanggan toEntity(MasterPelangganWrapper wrapper) {
		MasterPelanggan entity = new MasterPelanggan();
		if (wrapper.getIdPelanggan() != null) {
			entity = masterPelangganRepository.getReferenceById(wrapper.getIdPelanggan());
		}
		Optional<Users> optionalUsers = usersRepository.findById(wrapper.getUserId());
		Users users = optionalUsers.isPresent() ? optionalUsers.get() : null;
		entity.setUsers(users);
		entity.setIdPelanggan(wrapper.getIdPelanggan());
		entity.setAlamat(wrapper.getAlamat());
		entity.setNama(wrapper.getNama());
		entity.setNoTelp(wrapper.getNoTelp());
		return entity;
	}
	//method dalam service untuk menampilkan semua list
	private List<MasterPelangganWrapper> toWrapperList(List<MasterPelanggan> entityList) {
		List<MasterPelangganWrapper> wrapperList = new ArrayList<MasterPelangganWrapper>();
		for (MasterPelanggan entity : entityList) {
			MasterPelangganWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public PaginationList<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(int page, int size){
		Pageable paging = PageRequest.of(page, size);
		Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findAll(paging);
		List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
		List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
		return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
	}
	public PaginationList<MasterPelangganWrapper, MasterPelanggan> findAllWithPaginationFilter(TeleponPagingRequestWrapper wrapper){
		Pageable paging;
		if (wrapper.getSortOrder() == "ASC") {
			paging = PageRequest.of(wrapper.getPage(), wrapper.getSize(),Sort.by(Order.by(wrapper.getSortField())).ascending());
		} else {
			paging = PageRequest.of(wrapper.getPage(), wrapper.getSize(),Sort.by(Order.by(wrapper.getSortField())).descending());
		}
		List<TeleponFilterWrapper> filterWrapper = wrapper.getFilters();
		Long idPelanggan = 0L;
		String nama = null;
		Long noTelp = null;
		String alamat = null;
		Long userId = 0L;
		for (int i=0; i<filterWrapper.size(); i++) {
			switch(wrapper.getFilters().get(i).getName().toLowerCase()) {
			  case "idpelanggan":
				  idPelanggan = Long.valueOf(wrapper.getFilters().get(i).getValue()) ;
				  break;
			  case "nama":
				 nama = wrapper.getFilters().get(i).getValue() ;
				 break;
			  case "notelp":
				  noTelp = Long.valueOf(wrapper.getFilters().get(i).getValue()) ; 
				  break;
			  case "alamat": 
				  alamat = wrapper.getFilters().get(i).getValue() ;
				  break;
			  case "userid":
				  userId =   Long.valueOf(wrapper.getFilters().get(i).getValue()) ; 
				  break;
			  default:
			    // code block
			}
		}
		Optional<Users> optionalUser = usersRepository.findById(userId);
		Users users = optionalUser.isPresent() ? optionalUser.get() : null;
		Page<MasterPelanggan> masterPelangganPage = masterPelangganRepository.findByidPelangganOrNamaIgnoreCaseContainingOrAlamatIgnoreCaseContainingOrNoTelpOrUsers(paging,idPelanggan,nama,alamat,noTelp,users);
		List<MasterPelanggan> masterPelangganList =  masterPelangganPage.getContent();
		List<MasterPelangganWrapper> masterPelangganWrapperList = toWrapperList(masterPelangganList);
		return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);
	}
	
	
	
	
	 public ByteArrayInputStream load() {
		    List<MasterPelanggan> masterPelanggan = masterPelangganRepository.findAll();
		    ByteArrayInputStream in = ExcelHelperMasterPelanggan.tutorialsToExcel(masterPelanggan);
		    return in;
		  }
	 public List<MasterPelanggan> listAll() {
	        return masterPelangganRepository.findAll();
	    }
	 public void ExportToPdf(HttpServletResponse response) throws Exception {
			// Call the findAll method to retrieve the data
			List<MasterPelanggan> dataHistory = masterPelangganRepository.findAll();
			List<MasterPelangganWrapper> masterPelangganList = new ArrayList<MasterPelangganWrapper>();

			for (int i = 0; i < dataHistory.size(); i++) {
				MasterPelangganWrapper wrapper = new MasterPelangganWrapper();
				wrapper.setIdPelanggan(dataHistory.get(i).getIdPelanggan());
				wrapper.setNama(dataHistory.get(i).getNama());
				wrapper.setAlamat(dataHistory.get(i).getAlamat());
				wrapper.setNoTelp(dataHistory.get(i).getNoTelp());
//				wrapper.setUserId(dataHistory.get(i).getUsers() != null ? dataHistory.get(i).getUsers().getUserId() : null);
				masterPelangganList.add(wrapper);
			}

			// Now create a new iText PDF document
			Document pdfDoc = new Document(PageSize.A4.rotate());
			PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
			pdfDoc.open();

			Paragraph title = new Paragraph("Daftar Pelanggan", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
			title.setAlignment(Element.ALIGN_CENTER);
			pdfDoc.add(title);

			// Add the generation date
			pdfDoc.add(new Paragraph(
					"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

			// Create a table
			PdfPTable pdfTable = new PdfPTable(4);

			pdfTable.setWidthPercentage(100);
			pdfTable.setSpacingBefore(10f);
			pdfTable.setSpacingAfter(10f);

			PdfPCell cell1 = new PdfPCell(new Phrase("ID Pelanggan"));
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			pdfTable.addCell(cell1);
			PdfPCell cell2 = new PdfPCell(new Phrase("Nama Pelanggan"));
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			pdfTable.addCell(cell2);
			PdfPCell cell3 = new PdfPCell(new Phrase("Alamat"));
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			pdfTable.addCell(cell3);
			PdfPCell cell4 = new PdfPCell(new Phrase("No Telp"));
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			pdfTable.addCell(cell4);
			
			
			BaseColor color = new BaseColor(135, 206, 235);

			for (int i = 0; i < 4; i++) {
				pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
			}

			// Iterate through the data and add it to the table
			for (MasterPelangganWrapper entity : masterPelangganList) {
				pdfTable.addCell(String.valueOf(entity.getIdPelanggan() != null ? String.valueOf(entity.getIdPelanggan()) : "-"));
				pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
				pdfTable.addCell(String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-"));
				pdfTable.addCell(String.valueOf(entity.getNoTelp() != null ? String.valueOf(entity.getNoTelp()) : "-"));
				
			}
			// Add the table to the pdf document
			pdfDoc.add(pdfTable);

			pdfDoc.close();
			pdfWriter.close();

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
		}
	 
	 public PaginationList<MasterPelangganWrapper, MasterPelanggan> ListWithPaging(PagingRequestWrapper request) { 
			List<MasterPelanggan> masterPelangganList = masterPelangganCriteriaRepository.findByFilter(request);
			
			int fromIndex = (request.getPage())* (request.getSize());
			int toIndex = Math.min(fromIndex + request.getSize(), masterPelangganList.size());
			Page<MasterPelanggan> masterPelangganPage = new PageImpl<>(masterPelangganList.subList(fromIndex, toIndex), PageRequest.of(request.getPage(), request.getSize()), masterPelangganList.size());
			List<MasterPelangganWrapper> masterPelangganWrapperList = new ArrayList<MasterPelangganWrapper>();
			for(MasterPelanggan entity : masterPelangganPage) {
				MasterPelangganWrapper wrapper = toWrapper(entity);
			    masterPelangganWrapperList.add(wrapper);
			}
			return new PaginationList<MasterPelangganWrapper, MasterPelanggan>(masterPelangganWrapperList, masterPelangganPage);	
		}
//	 private List<MasterPelangganWrapper> toWrapperList(List<MasterPelanggan> entityList) {
//			List<MasterPelangganWrapper> wrapperList = new ArrayList<MasterPelangganWrapper>();
//			for (MasterPelanggan entity : entityList) {
//				MasterPelangganWrapper wrapper = toWrapper(entity);
//				wrapperList.add(wrapper);
//			}
//			return wrapperList;
//		}
	 public MasterPelanggan findByNama(String nama) {
		 MasterPelanggan masterPelanggan = masterPelangganRepository.findByNama(nama);
		 return masterPelanggan;
	 }
	
}

package com.ogya.lokakarya.service.bankadm;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.entity.bankadm.MasterBank;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.exercise.feign.request.bankadm.BankAdminFeignRequest;
import com.ogya.lokakarya.exercise.feign.response.bankadm.BankAdminFeignResponse;
import com.ogya.lokakarya.exercise.feign.services.bankadm.BankAdminFeignServices;
import com.ogya.lokakarya.repository.bankadm.MasterBankCriteriaRepository;
import com.ogya.lokakarya.repository.bankadm.MasterBankRepository;
import com.ogya.lokakarya.util.DataResponseFeign;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.bankadm.MasterBankWrapper;


@Service
@Transactional
public class MasterBankService {
	@Autowired
	MasterBankRepository masterBankRepository;
	@Autowired
	MasterBankCriteriaRepository masterBankCriteriaRepository;

    @Autowired
    BankAdminFeignServices bankAdminFeignServices;

	public PaginationList<MasterBankWrapper, MasterBank> ListWithPaging(PagingRequestWrapper request) { 
		List<MasterBank> masterBankList = masterBankCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage())* (request.getSize());
		int toIndex = Math.min(fromIndex + request.getSize(), masterBankList.size());
		Page<MasterBank> masterBankPage = new PageImpl<>(masterBankList.subList(fromIndex, toIndex), PageRequest.of(request.getPage(), request.getSize()), masterBankList.size());
		List<MasterBankWrapper> masterBankWrapperList = new ArrayList<>();
		for(MasterBank entity : masterBankPage) {
		    masterBankWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<MasterBankWrapper, MasterBank>(masterBankWrapperList, masterBankPage);	
	}
	
	public MasterBankWrapper getByNoRek(Long norek) {
		MasterBank masterbank = masterBankRepository.getReferenceById(norek);
		return toWrapper(masterbank);
	}
	
	private MasterBankWrapper toWrapper(MasterBank entity) {
		MasterBankWrapper wrapper = new MasterBankWrapper();
		wrapper.setNorek(entity.getNorek());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setNotlp(entity.getNotlp());
		wrapper.setSaldo(entity.getSaldo());
		wrapper.setUserId(entity.getUserId());
		return wrapper;
	}
	
	private List<MasterBankWrapper> toWrapperList(List<MasterBank> entityList){
		List<MasterBankWrapper> wrapperList = new ArrayList<MasterBankWrapper>();
		for(MasterBank entity : entityList) {
			MasterBankWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public List<MasterBankWrapper> findAll() {
		List<MasterBank> employeeList = masterBankRepository.findAll(Sort.by(Order.by("norek")).descending());
		return toWrapperList(employeeList);
	}
	
	private MasterBank toEntity(MasterBankWrapper wrapper) {
		MasterBank entity = new MasterBank();
		if (wrapper.getNorek() != null) {
			entity = masterBankRepository.getReferenceById(wrapper.getNorek());
		}
		entity.setNorek(wrapper.getNorek());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setNotlp(wrapper.getNotlp());
		entity.setSaldo(wrapper.getSaldo());
		entity.setUserId(wrapper.getUserId());
		return entity;
	}
	
	public DataResponseFeign<MasterBankWrapper> save(MasterBankWrapper wrapper) {
	    DataResponseFeign<MasterBankWrapper> dataResponse = new DataResponseFeign<MasterBankWrapper>();
	    try {
	        BankAdminFeignRequest request = new BankAdminFeignRequest();
	        request.setAlamat(wrapper.getAlamat());
	        request.setNama(wrapper.getNama());
	        request.setNominalSaldo(wrapper.getSaldo());
	        request.setTelpon(wrapper.getNotlp().toString());
	        BankAdminFeignResponse response = bankAdminFeignServices.bankPost(request);
	        if (response.getSuccess()) {
	            MasterBank employee = masterBankRepository.save(toEntity(wrapper));
	            dataResponse.setSuccess(true);
	            dataResponse.setReferenceNumber(response.getReferenceNumber());
	            dataResponse.setData(toWrapper(employee));
	        } else {
	            throw new Exception("Failed to save employee");
	        }
	        return dataResponse;
	    } catch (Exception e) {
	        //log the exception here
	        //You can also return a custom message for user 
	        return new DataResponseFeign<MasterBankWrapper>(false, e.getMessage(), null);
	    }
	}



	/*
	 * public MasterBankWrapper save(MasterBankWrapper wrapper) { MasterBank
	 * employee = masterBankRepository.save(toEntity(wrapper));
	 * BankAdminFeignRequest request = new BankAdminFeignRequest(); // set
	 * properties of request object BankAdminFeignResponse response =
	 * bankAdminFeignServices.bankPost(request); return toWrapper(employee); }
	 */
	
	public void delete(Long norek) {
		if(masterBankRepository.isExistMasterBank(norek) != 0)
				if (masterBankRepository.isExistHistoyBank(norek) == 0) {
					masterBankRepository.deleteById(norek);
				} else {
					throw new BusinessException("NASABAH cannot deleted. REK. NUMBER is still used in the HISTORY table");
				}else {
					throw new BusinessException("NASABAH with REK. NUMBER inputed is not Exist!");
				}
	
}
	
	public PaginationList<MasterBankWrapper, MasterBank> findAllWithPagination(int page, int size){
		Pageable paging = PageRequest.of(page, size);
		Page<MasterBank> bankPage = masterBankRepository.findAll(paging);
		List<MasterBank> bankList =  bankPage.getContent();
		List<MasterBankWrapper> bookWrapperList = toWrapperList(bankList);
		return new PaginationList<MasterBankWrapper, MasterBank>(bookWrapperList, bankPage);
	}
	
	

	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	
	public void exportToPdf(HttpServletResponse response) throws Exception {
	    // Call the findAll method to retrieve the data
	    List<MasterBank> data = masterBankRepository.findAll();

	    // Read the properties file to get the column names
	    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("column/columnBankAdm.properties");
	    Properties properties = new Properties();
	    properties.load(inputStream);
	    List<String> columnNames = new ArrayList<>(properties.stringPropertyNames());

	    // Now create a new iText PDF document
	    Document pdfDoc = new Document(PageSize.A4.rotate());
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
	    pdfDoc.open();

	    Paragraph title = new Paragraph("Laporan Data Nasabah Bank", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);
	    // Add the generation date
	    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

	    // Create a table
	    PdfPTable pdfTable = new PdfPTable(columnNames.size());
	    pdfTable.setWidthPercentage(100);
	    pdfTable.setSpacingBefore(10f);
	    pdfTable.setSpacingAfter(10f);

	    // Add column names to the table
	    for (String columnName : columnNames) {
	        pdfTable.addCell(Align(properties.getProperty(columnName)));
	    }
	    // set the first row to gray
	    for (int i = 0; i < columnNames.size(); i++) {
	        pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
	    }

	    // Iterate through the data and add it to the table
	    for (MasterBank entity : data) {
	        for (String columnName : columnNames) {
	            String value = "";
	            if (columnName.equals("Nomor Rekening")) {
	                value = String.valueOf(entity.getNorek() != null ? String.valueOf(entity.getNorek()) : "-");
	            } else if (columnName.equals("Nama")) {
	                value = String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-");
	            } else if (columnName.equals("Alamat")) {
	                value = String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-");
	            } else if (columnName.equals("No Telepon")) {
	                value = String.valueOf(entity.getNotlp() != null ? String.valueOf(entity.getNotlp()) : "-");
	            } else if (columnName.equals("Saldo")) {
	                value = String.valueOf(entity.getSaldo() != null ? String.valueOf(entity.getSaldo()) : "-");
	            }
	            pdfTable.addCell(Align(value));
	        }
	    }

	    // Add the table to the pdf document
	    pdfDoc.add(pdfTable);

	    pdfDoc.close();
	    pdfWriter.close();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
	
	/*
	 * public void ExportToPdf(HttpServletResponse response) throws Exception{ //
	 * Call the findAll method to retrieve the data List<MasterBank> data =
	 * masterBankRepository.findAll();
	 * 
	 * // Now create a new iText PDF document Document pdfDoc = new
	 * Document(PageSize.A4.rotate()); PdfWriter pdfWriter =
	 * PdfWriter.getInstance(pdfDoc, response.getOutputStream()); pdfDoc.open();
	 * 
	 * Paragraph title = new Paragraph("Laporan Data Nasabah Bank", new
	 * Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	 * title.setAlignment(Element.ALIGN_CENTER); pdfDoc.add(title); // // Add the
	 * generation date pdfDoc.add(new Paragraph("Report generated on: " + new
	 * SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
	 * 
	 * // Create a table PdfPTable pdfTable = new PdfPTable(5);
	 * 
	 * 
	 * pdfTable.setWidthPercentage(100); pdfTable.setSpacingBefore(10f);
	 * pdfTable.setSpacingAfter(10f);
	 * 
	 * 
	 * pdfTable.addCell(Align("Nomor Rekening")); pdfTable.addCell(Align("Nama"));
	 * pdfTable.addCell(Align("Alamat")); pdfTable.addCell(Align("No Telepon"));
	 * pdfTable.addCell(Align("Saldo"));
	 * 
	 * for(int i=0;i<5;i++) { pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f); }
	 * 
	 * // Iterate through the data and add it to the table for (MasterBank entity :
	 * data) { pdfTable.addCell(Align(String.valueOf(entity.getNorek() != null ?
	 * String.valueOf(entity.getNorek()) : "-")));
	 * pdfTable.addCell(Align(String.valueOf(entity.getNama() != null ?
	 * String.valueOf(entity.getNama()) : "-")));
	 * pdfTable.addCell(Align(String.valueOf(entity.getAlamat() != null ?
	 * String.valueOf(entity.getAlamat()) : "-")));
	 * pdfTable.addCell(Align(String.valueOf(entity.getNotlp() != null ?
	 * String.valueOf(entity.getNotlp()) : "-")));
	 * pdfTable.addCell(Align(String.valueOf(entity.getSaldo() != null ?
	 * String.valueOf(entity.getSaldo()) : "-")));
	 * 
	 * //
	 * 
	 * }
	 * 
	 * // Add the table to the pdf document pdfDoc.add(pdfTable);
	 * 
	 * pdfDoc.close(); pdfWriter.close();
	 * 
	 * response.setContentType("application/pdf");
	 * response.setHeader("Content-Disposition",
	 * "attachment; filename=exportedPdf.pdf"); }
	 */
	 public void exportToXls(HttpServletResponse response) throws Exception {
	        // Call the findAll method to retrieve the data
	        List<MasterBank> data = masterBankRepository.findAll();

	        // Create a new Apache POI workbook
	        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
	            HSSFSheet sheet = workbook.createSheet("Laporan Transaksi Bank");

	            // Create the header row
	            HSSFRow headerRow = sheet.createRow(0);
	            headerRow.createCell(0).setCellValue("Nomor Rekening");
	            headerRow.createCell(1).setCellValue("Nama");
	            headerRow.createCell(2).setCellValue("Alamat");
	            headerRow.createCell(3).setCellValue("No Telepon");
	            headerRow.createCell(4).setCellValue("Saldo");

	            // Iterate through the data and add it to the sheet
	            int rowNum = 1;
	            for (MasterBank entity : data) {
	                HSSFRow row = sheet.createRow(rowNum++);
	                row.createCell(0).setCellValue(entity.getNorek() != null ? String.valueOf(entity.getNorek()) : "-");
	                row.createCell(1).setCellValue(entity.getNama() != null ? entity.getNama() : "-");
	                row.createCell(2).setCellValue(entity.getAlamat() != null ? entity.getAlamat() : "-");
	                row.createCell(0).setCellValue(entity.getNotlp() != null ? String.valueOf(entity.getNotlp()) : "-");
	                row.createCell(0).setCellValue(entity.getSaldo() != null ? String.valueOf(entity.getSaldo()) : "-");
	            }

	            // Write the workbook to the response output stream
	            response.setContentType("application/vnd.ms-excel");
	            response.setHeader("Content-Disposition", "attachment; filename=exportedXls.xls");
	            workbook.write(response.getOutputStream());
	            response.flushBuffer();
	        }
	    }
}


package com.ogya.lokakarya.bankadm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.repository.MasterBankRepository;
import com.ogya.lokakarya.bankadm.wrapper.HistoryBankWrapper;
import com.ogya.lokakarya.util.PaginationList;

@Service
@Transactional
public class HistoryBankService {
@Autowired
HistoryBankRepository historyBankRepository;
@Autowired
MasterBankRepository masterBankRepository;

public HistoryBankWrapper getByidHistoryBank(Long idHistoryBank) {
	HistoryBank historybank = historyBankRepository.getReferenceById(idHistoryBank);
	return toWrapper(historybank);
}
public Long sumStatus1() {
	Long historybank = historyBankRepository.sumStatus1();
	return historybank;
}

public Long sumStatus2() {
	Long historybank = historyBankRepository.sumStatus2();
	return historybank;
}

public Long sumStatus3() {
	Long historybank = historyBankRepository.sumStatus3();
	return historybank;
}

public Long sumStatus4() {
	Long historybank = historyBankRepository.sumStatus4();
	return historybank;
}

private HistoryBankWrapper toWrapper(HistoryBank entity) {
	HistoryBankWrapper wrapper = new HistoryBankWrapper();
	wrapper.setIdHistoryBank(entity.getIdHistoryBank());
	wrapper.setNorek(entity.getRekening() != null ? entity.getRekening().getNorek() : null);
	wrapper.setNama(entity.getNama());
	wrapper.setTanggal(entity.getTanggal());
	wrapper.setUang(entity.getUang());
	wrapper.setStatusKet(entity.getStatusKet());
	wrapper.setNoRekTujuan(entity.getNoRekTujuan());
	wrapper.setNoTlp(entity.getNoTlp());
	wrapper.setNamaTujuan(entity.getNamaTujuan());
	return wrapper;
}

private List<HistoryBankWrapper> toWrapperList(List<HistoryBank> entityList){
	List<HistoryBankWrapper> wrapperList = new ArrayList<HistoryBankWrapper>();
	for( HistoryBank entity : entityList) {
		HistoryBankWrapper wrapper = toWrapper(entity);
		wrapperList.add(wrapper);
	}
	return wrapperList;
}

public List<HistoryBankWrapper> findAll() {
	List<HistoryBank> employeeList = historyBankRepository.findAll(Sort.by(Order.by("idHistoryBank")).descending());
	return toWrapperList(employeeList);
}

private HistoryBank toEntity(HistoryBankWrapper wrapper) {
	HistoryBank entity = new HistoryBank();
	if (wrapper.getIdHistoryBank() != null) {
		entity = historyBankRepository.getReferenceById(wrapper.getIdHistoryBank());
	}
	entity.setIdHistoryBank(wrapper.getIdHistoryBank());
	Optional<MasterBank> optionalRek = masterBankRepository.findById(wrapper.getNorek());
	MasterBank rekening = optionalRek.isPresent() ? optionalRek.get() : null;
	entity.setRekening(rekening);
	entity.setTanggal(wrapper.getTanggal());
	entity.setUang(wrapper.getUang());
	entity.setStatusKet(wrapper.getStatusKet());
	entity.setNoRekTujuan(wrapper.getNoRekTujuan());
	entity.setNoTlp(wrapper.getNoTlp());
	entity.setNamaTujuan(wrapper.getNamaTujuan());
	return entity;
}

public HistoryBankWrapper save(HistoryBankWrapper wrapper) {
	HistoryBank employee = historyBankRepository.save(toEntity(wrapper));
	return toWrapper(employee);
}

public void delete(Long idHistoryBank) {
	historyBankRepository.deleteById(idHistoryBank);
}

public List<HistoryBankWrapper> getBystatusKet(Byte statusKet) {
	List<HistoryBank> historybank =  historyBankRepository.findByStatusKet(statusKet);
	return toWrapperList(historybank);
}

public PaginationList<HistoryBankWrapper, HistoryBank> findAllWithPagination(int page, int size){
	Pageable paging = PageRequest.of(page, size);
	Page<HistoryBank> historyPage = historyBankRepository.findAll(paging);
	List<HistoryBank> historyList =  historyPage.getContent();
	List<HistoryBankWrapper> historyWrapperList = toWrapperList(historyList);
	return new PaginationList<HistoryBankWrapper, HistoryBank>(historyWrapperList, historyPage);
}

public PaginationList<HistoryBankWrapper, HistoryBank> findByStatusKetPagination(Byte statusKet, int page, int size) {
	Pageable paging = PageRequest.of(page, size);
	Page<HistoryBank> historyPage = historyBankRepository.findByStatusKet( statusKet, paging);
	List<HistoryBank> historyList = historyPage.getContent();
	List<HistoryBankWrapper> historyWrapperList = toWrapperList(historyList);
	return new PaginationList<HistoryBankWrapper, HistoryBank>(historyWrapperList, historyPage);
}

public void ExportToPdf(HttpServletResponse response) throws Exception{
	 // Call the findAll method to retrieve the data
    List<HistoryBank> data = historyBankRepository.findAll();
    
    // Now create a new iText PDF document
    Document pdfDoc = new Document(PageSize.A4.rotate());
    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
    pdfDoc.open();
    
    Paragraph title = new Paragraph("Laporan Transaksi Bank",
            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
    title.setAlignment(Element.ALIGN_CENTER);
    pdfDoc.add(title);
    
    // Add the generation date
    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

    // Create a table
    PdfPTable pdfTable = new PdfPTable(8); 
    

    pdfTable.setWidthPercentage(100);
    pdfTable.setSpacingBefore(10f);
    pdfTable.setSpacingAfter(10f);
         
  
        pdfTable.addCell("Nomor Rekening");
        pdfTable.addCell("Nama");
        pdfTable.addCell("Tanggal Transaksi");
        pdfTable.addCell("Nominal");
        pdfTable.addCell("Keterangan");
        pdfTable.addCell("Rekening Tujuan");
        pdfTable.addCell("Tujuan Nama");
        pdfTable.addCell("No. TLP");        	
    	for(int i=0;i<8;i++) {
    		pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
    	}
    
    // Iterate through the data and add it to the table
    for (HistoryBank entity : data) {
    	pdfTable.addCell(String.valueOf(entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-"));
    	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	String formattedDate = "-";
    	if (entity.getTanggal() != null) {
    	   formattedDate = formatter.format(entity.getTanggal());
    	}
    	pdfTable.addCell(formattedDate);
    	pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));

    	String status = "-";
    	if (entity.getStatusKet() != null) {
    	    if (entity.getStatusKet() == 1) {
    	        status = "Setor";
    	    } else if (entity.getStatusKet() == 2) {
    	        status = "Tarik";
    	    }else if (entity.getStatusKet() == 3) {
    	        status = "Transfer";
    	    }else if (entity.getStatusKet() == 4) {
    	        status = "Bayar Telepon";
    	    }
    	}
    	pdfTable.addCell(status);
    	
    	pdfTable.addCell(String.valueOf(entity.getNoRekTujuan() != null ? String.valueOf(entity.getNoRekTujuan()) : "-"));
    	pdfTable.addCell(String.valueOf(entity.getNamaTujuan() != null ? String.valueOf(entity.getNamaTujuan()) : "-"));
    	pdfTable.addCell(String.valueOf(entity.getNoTlp() != null ? String.valueOf(entity.getNoTlp()) : "-"));
//

    }
    
    // Add the table to the pdf document
    pdfDoc.add(pdfTable);

    pdfDoc.close();
    pdfWriter.close();

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
}


public void ExportToPdfSetor(HttpServletResponse response) throws Exception{
	 // Call the findAll method to retrieve the data
   List<HistoryBank> data = historyBankRepository.laporanSetor();
   
   // Now create a new iText PDF document
   Document pdfDoc = new Document(PageSize.A4.rotate());
   PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
   pdfDoc.open();
   
   Paragraph title = new Paragraph("Laporan Transaksi Bank",
           new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
   title.setAlignment(Element.ALIGN_CENTER);
   pdfDoc.add(title);
   
   // Add the generation date
   pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

   // Create a table
   PdfPTable pdfTable = new PdfPTable(8); 
   

   pdfTable.setWidthPercentage(100);
   pdfTable.setSpacingBefore(10f);
   pdfTable.setSpacingAfter(10f);
        
 
       pdfTable.addCell("Nomor Rekening");
       pdfTable.addCell("Nama");
       pdfTable.addCell("Tanggal Transaksi");
       pdfTable.addCell("Nominal");
       pdfTable.addCell("Keterangan");
       pdfTable.addCell("Rekening Tujuan");
       pdfTable.addCell("Tujuan Nama");
       pdfTable.addCell("No. TLP");        	
   	for(int i=0;i<8;i++) {
   		pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
   	}
   
   // Iterate through the data and add it to the table
   for (HistoryBank entity : data) {
   	pdfTable.addCell(String.valueOf(entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-"));
   	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
   	
   	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
   	String formattedDate = "-";
   	if (entity.getTanggal() != null) {
   	   formattedDate = formatter.format(entity.getTanggal());
   	}
   	pdfTable.addCell(formattedDate);
   	pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));

   	String status = "-";
   	if (entity.getStatusKet() != null) {
   	    if (entity.getStatusKet() == 1) {
   	        status = "Setor";
   	    } else if (entity.getStatusKet() == 2) {
   	        status = "Tarik";
   	    }else if (entity.getStatusKet() == 3) {
   	        status = "Transfer";
   	    }else if (entity.getStatusKet() == 4) {
   	        status = "Bayar Telepon";
   	    }
   	}
   	pdfTable.addCell(status);
   	
   	pdfTable.addCell(String.valueOf(entity.getNoRekTujuan() != null ? String.valueOf(entity.getNoRekTujuan()) : "-"));
   	pdfTable.addCell(String.valueOf(entity.getNamaTujuan() != null ? String.valueOf(entity.getNamaTujuan()) : "-"));
   	pdfTable.addCell(String.valueOf(entity.getNoTlp() != null ? String.valueOf(entity.getNoTlp()) : "-"));
//

   }
   
   // Add the table to the pdf document
   pdfDoc.add(pdfTable);

   pdfDoc.close();
   pdfWriter.close();

   response.setContentType("application/pdf");
   response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
}



public void ExportToPdfTarik(HttpServletResponse response) throws Exception{
	 // Call the findAll method to retrieve the data
   List<HistoryBank> data = historyBankRepository.laporanTarik();
   
   // Now create a new iText PDF document
   Document pdfDoc = new Document(PageSize.A4.rotate());
   PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
   pdfDoc.open();
   
   Paragraph title = new Paragraph("Laporan Transaksi Bank",
           new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
   title.setAlignment(Element.ALIGN_CENTER);
   pdfDoc.add(title);
   
   // Add the generation date
   pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

   // Create a table
   PdfPTable pdfTable = new PdfPTable(8); 
   

   pdfTable.setWidthPercentage(100);
   pdfTable.setSpacingBefore(10f);
   pdfTable.setSpacingAfter(10f);
        
 
       pdfTable.addCell("Nomor Rekening");
       pdfTable.addCell("Nama");
       pdfTable.addCell("Tanggal Transaksi");
       pdfTable.addCell("Nominal");
       pdfTable.addCell("Keterangan");
       pdfTable.addCell("Rekening Tujuan");
       pdfTable.addCell("Tujuan Nama");
       pdfTable.addCell("No. TLP");        	
   	for(int i=0;i<8;i++) {
   		pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
   	}
   
   // Iterate through the data and add it to the table
   for (HistoryBank entity : data) {
   	pdfTable.addCell(String.valueOf(entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-"));
   	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
   	
   	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
   	String formattedDate = "-";
   	if (entity.getTanggal() != null) {
   	   formattedDate = formatter.format(entity.getTanggal());
   	}
   	pdfTable.addCell(formattedDate);
   	pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));

   	String status = "-";
   	if (entity.getStatusKet() != null) {
   	    if (entity.getStatusKet() == 1) {
   	        status = "Setor";
   	    } else if (entity.getStatusKet() == 2) {
   	        status = "Tarik";
   	    }else if (entity.getStatusKet() == 3) {
   	        status = "Transfer";
   	    }else if (entity.getStatusKet() == 4) {
   	        status = "Bayar Telepon";
   	    }
   	}
   	pdfTable.addCell(status);
   	
   	pdfTable.addCell(String.valueOf(entity.getNoRekTujuan() != null ? String.valueOf(entity.getNoRekTujuan()) : "-"));
   	pdfTable.addCell(String.valueOf(entity.getNamaTujuan() != null ? String.valueOf(entity.getNamaTujuan()) : "-"));
   	pdfTable.addCell(String.valueOf(entity.getNoTlp() != null ? String.valueOf(entity.getNoTlp()) : "-"));
//

   }
   
   // Add the table to the pdf document
   pdfDoc.add(pdfTable);

   pdfDoc.close();
   pdfWriter.close();

   response.setContentType("application/pdf");
   response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
}


public void ExportToPdfTransfer(HttpServletResponse response) throws Exception{
	 // Call the findAll method to retrieve the data
  List<HistoryBank> data = historyBankRepository.laporanTransfer();
  
  // Now create a new iText PDF document
  Document pdfDoc = new Document(PageSize.A4.rotate());
  PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
  pdfDoc.open();
  
  Paragraph title = new Paragraph("Laporan Transaksi Bank",
          new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
  title.setAlignment(Element.ALIGN_CENTER);
  pdfDoc.add(title);
  
  // Add the generation date
  pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

  // Create a table
  PdfPTable pdfTable = new PdfPTable(8); 
  

  pdfTable.setWidthPercentage(100);
  pdfTable.setSpacingBefore(10f);
  pdfTable.setSpacingAfter(10f);
       
      pdfTable.addCell("Nomor Rekening");
      pdfTable.addCell("Nama");
      pdfTable.addCell("Tanggal Transaksi");
      pdfTable.addCell("Nominal");
      pdfTable.addCell("Keterangan");
      pdfTable.addCell("Rekening Tujuan");
      pdfTable.addCell("Tujuan Nama");
      pdfTable.addCell("No. TLP");        	
  	for(int i=0;i<8;i++) {
  		pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
  	}
  
  // Iterate through the data and add it to the table
  for (HistoryBank entity : data) {
  	pdfTable.addCell(String.valueOf(entity.getIdHistoryBank() != null ? String.valueOf(entity.getIdHistoryBank()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
  	
  	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
  	String formattedDate = "-";
  	if (entity.getTanggal() != null) {
  	   formattedDate = formatter.format(entity.getTanggal());
  	}
  	pdfTable.addCell(formattedDate);
  	pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));

  	String status = "-";
  	if (entity.getStatusKet() != null) {
  	    if (entity.getStatusKet() == 1) {
  	        status = "Setor";
  	    } else if (entity.getStatusKet() == 2) {
  	        status = "Tarik";
  	    }else if (entity.getStatusKet() == 3) {
  	        status = "Transfer";
  	    }else if (entity.getStatusKet() == 4) {
  	        status = "Bayar Telepon";
  	    }
  	}
  	pdfTable.addCell(status);
  	
  	pdfTable.addCell(String.valueOf(entity.getNoRekTujuan() != null ? String.valueOf(entity.getNoRekTujuan()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getNamaTujuan() != null ? String.valueOf(entity.getNamaTujuan()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getNoTlp() != null ? String.valueOf(entity.getNoTlp()) : "-"));
//

  }
  
  // Add the table to the pdf document
  pdfDoc.add(pdfTable);

  pdfDoc.close();
  pdfWriter.close();

  response.setContentType("application/pdf");
  response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
}

public void ExportToPdfBayarTelepon(HttpServletResponse response) throws Exception{
	 // Call the findAll method to retrieve the data
  List<HistoryBank> data = historyBankRepository.laporanBayarTelepon();
  
  // Now create a new iText PDF document
  Document pdfDoc = new Document(PageSize.A4.rotate());
  PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
  pdfDoc.open();
  
  Paragraph title = new Paragraph("Laporan Transaksi Bank",
          new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
  title.setAlignment(Element.ALIGN_CENTER);
  pdfDoc.add(title);
  
  // Add the generation date
  pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

  // Create a table
  PdfPTable pdfTable = new PdfPTable(8); 
  

  pdfTable.setWidthPercentage(100);
  pdfTable.setSpacingBefore(10f);
  pdfTable.setSpacingAfter(10f);
       

      pdfTable.addCell("Nomor Rekening");
      pdfTable.addCell("Nama");
      pdfTable.addCell("Tanggal Transaksi");
      pdfTable.addCell("Nominal");
      pdfTable.addCell("Keterangan");
      pdfTable.addCell("Rekening Tujuan");
      pdfTable.addCell("Tujuan Nama");
      pdfTable.addCell("No. TLP");        	
  	for(int i=0;i<8;i++) {
  		pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
  	}
  
  // Iterate through the data and add it to the table
  for (HistoryBank entity : data) {
  	pdfTable.addCell(String.valueOf(entity.getIdHistoryBank() != null ? String.valueOf(entity.getIdHistoryBank()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
  	
  	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
  	String formattedDate = "-";
  	if (entity.getTanggal() != null) {
  	   formattedDate = formatter.format(entity.getTanggal());
  	}
  	pdfTable.addCell(formattedDate);
  	pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));

  	String status = "-";
  	if (entity.getStatusKet() != null) {
  	    if (entity.getStatusKet() == 1) {
  	        status = "Setor";
  	    } else if (entity.getStatusKet() == 2) {
  	        status = "Tarik";
  	    }else if (entity.getStatusKet() == 3) {
  	        status = "Transfer";
  	    }else if (entity.getStatusKet() == 4) {
  	        status = "Bayar Telepon";
  	    }
  	}
  	pdfTable.addCell(status);
  	
  	pdfTable.addCell(String.valueOf(entity.getNoRekTujuan() != null ? String.valueOf(entity.getNoRekTujuan()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getNamaTujuan() != null ? String.valueOf(entity.getNamaTujuan()) : "-"));
  	pdfTable.addCell(String.valueOf(entity.getNoTlp() != null ? String.valueOf(entity.getNoTlp()) : "-"));
//

  }
  
  // Add the table to the pdf document
  pdfDoc.add(pdfTable);

  pdfDoc.close();
  pdfWriter.close();

  response.setContentType("application/pdf");
  response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
}


public PaginationList<HistoryBankWrapper, HistoryBank> findByFilter(String keyfilter,String sortField,String sortOrder, int page, int size) {
	Pageable paging = PageRequest.of(page, size);
	Page<HistoryBank> historyPage = historyBankRepository.findAllFilter(keyfilter, sortField, sortOrder, paging);
	List<HistoryBank> historyList =  historyPage.getContent();
	List<HistoryBankWrapper> historyWrapperList = toWrapperList(historyList);
	return new PaginationList<HistoryBankWrapper, HistoryBank>(historyWrapperList, historyPage);
	}

}

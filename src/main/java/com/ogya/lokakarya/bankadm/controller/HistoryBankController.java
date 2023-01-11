package com.ogya.lokakarya.bankadm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.service.HistoryBankService;
import com.ogya.lokakarya.bankadm.wrapper.HistoryBankWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;

@RestController
@RequestMapping(value = "/historybank")
@CrossOrigin(origins = "*")
public class HistoryBankController {
	@Autowired
	HistoryBankRepository historyBankRepository;
	@Autowired
	HistoryBankService historyBankService;
	
	@GetMapping(path = "/getByIdPlan")
	public HistoryBankWrapper getByidHistoryBank(@RequestParam("id") Long idHistoryBank) {
		return historyBankService.getByidHistoryBank(idHistoryBank);
	}
	
	@GetMapping(path = "/getById")
	public DataResponse<HistoryBankWrapper> getByidTransaksiNasabah(@RequestParam("id") Long idHistoryBank) {
		return new DataResponse<HistoryBankWrapper>(historyBankService.getByidHistoryBank(idHistoryBank));
	}
	
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<HistoryBankWrapper, HistoryBank> findAllWithPagination(@RequestParam("page") 
	int page, @RequestParam("size") int size) {
		return new DataResponsePagination<HistoryBankWrapper, HistoryBank>(historyBankService.findAllWithPagination(page, size));
	}
	
	
	@GetMapping(path = "/getByStatusKet")
	public DataResponseList<HistoryBankWrapper> getByStatusKetTransaksiNasabah(@RequestParam("statusKet") Byte statusKet) {
		return new DataResponseList<HistoryBankWrapper>(historyBankService.getBystatusKet(statusKet));
	}
	
	@GetMapping(path = "/findByStatusKetPagination")
	public DataResponsePagination<HistoryBankWrapper, HistoryBank> findByStatusKetPagination(@RequestParam("statusKet") Byte statusKet ,@RequestParam("page") 
	int page, @RequestParam("size") int size) {
		return new DataResponsePagination<HistoryBankWrapper, HistoryBank>(historyBankService.findByStatusKetPagination(statusKet, page, size));
	}
	
	@GetMapping(path = "/sumStatus1")
	public Long sumStatus1() {
		return historyBankService.sumStatus1();
	}
	
	@GetMapping(path = "/sumStatus2")
	public Long sumStatus2() {
		return historyBankService.sumStatus2();
	}
	
	@GetMapping(path = "/sumStatus3")
	public Long sumStatus3() {
		return historyBankService.sumStatus3();
	}
	
	@GetMapping(path = "/sumStatus4")
	public Long sumStatus4() {
		return historyBankService.sumStatus4();
	}
	
	@GetMapping(path = "/findAllPlan")
	public List<HistoryBankWrapper> findAllPlan() {
		return historyBankService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<HistoryBankWrapper> findAll() {
		return new DataResponseList<HistoryBankWrapper>(historyBankService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<HistoryBankWrapper> save(@RequestBody HistoryBankWrapper wrapper){
		return new DataResponse<HistoryBankWrapper>(historyBankService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<HistoryBankWrapper> update(@RequestBody HistoryBankWrapper wrapper){
		return new DataResponse<HistoryBankWrapper>(historyBankService.save(wrapper));
	}
	
	@DeleteMapping(path = "/deleteById") 
	public String deleteId(@RequestParam("id")  Long norek) {
		historyBankService.delete(norek);
		return "History "+norek+" Berhasil di delete";
	}
	
	
    @RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
    public void exportToPdf(HttpServletResponse response) throws Exception {
        
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
        PdfPTable pdfTable = new PdfPTable(9); 
        
  
        pdfTable.setWidthPercentage(100);
        pdfTable.setSpacingBefore(10f);
        pdfTable.setSpacingAfter(10f);
             
      
            pdfTable.addCell("ID History");
            pdfTable.addCell("Nomor Rekening");
            pdfTable.addCell("Nama");
            pdfTable.addCell("Tanggal Transaksi");
            pdfTable.addCell("Nominal");
            pdfTable.addCell("Keterangan");
            pdfTable.addCell("Rekening Tujuan");
            pdfTable.addCell("Tujuan Nama");
            pdfTable.addCell("No. TLP");        	
        	for(int i=0;i<9;i++) {
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


        }
        
        // Add the table to the pdf document
        pdfDoc.add(pdfTable);

        pdfDoc.close();
        pdfWriter.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
    }


}

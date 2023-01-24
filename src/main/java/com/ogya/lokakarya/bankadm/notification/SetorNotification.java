package com.ogya.lokakarya.bankadm.notification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.service.TransaksiNasabahService;
import com.ogya.lokakarya.util.CurrencyData;

@Service
public class SetorNotification {
	@Autowired
	private HistoryBankRepository historyBankRepo;
	@Autowired
	private TransaksiNasabahService transaksiNasabahService;
	
	//	Setiap hari jam 7
	@Scheduled(cron = "0 0 7 * * *")
	public void historyNotificationDaily() throws MessagingException, IOException, DocumentException {
		try {
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, -1);
	        
	        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	        String hari = date.format(cal.getTime());
			
			SimpleDateFormat tanggal = new SimpleDateFormat("EEEE dd/MM/yyyy", new Locale("in", "ID"));
			String day = tanggal.format(cal.getTime());

			List<HistoryBank> data = historyBankRepo.setorDaily(hari);
			Long jumlah = historyBankRepo.jumlahSetorHarian(hari);
			
			Long total = historyBankRepo.totalSetorHarian(hari);
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(total);
			
			ByteArrayOutputStream historySetorPdf = ExportToPdfSetor(data, jumlah, numberFormat.format(currencyNominal.getValue()).toString(), "Periode "+day);
			ByteArrayOutputStream historySetorXls = WriteExcelToEmail(data);
			
			Context ctx = new Context();
			ctx.setVariable("tanggal", day);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			transaksiNasabahService.sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai "+day,
					"History Setor " + day + ".pdf", "SetorHarian", ctx, historySetorPdf);
			transaksiNasabahService.sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai "+day,
					"History Setor " + day + ".xls", "SetorHarian", ctx, historySetorXls);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//	Setiap hari Senin jam 7
	@Scheduled(cron = "0 0 7 * * MON")
	public void historyNotificationWeekly() throws MessagingException, IOException, DocumentException {
		try {		
			Calendar calStart = Calendar.getInstance();
	        calStart.add(Calendar.DATE, -7);
	        Calendar calEnd = Calendar.getInstance();
	        calEnd.add(Calendar.DATE, -1);
	        
	        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	        String startDate = date.format(calStart.getTime());
	        String endDate = date.format(calEnd.getTime());
			
			SimpleDateFormat tanggal = new SimpleDateFormat("EEEE dd/MM/yyyy", new Locale("in", "ID"));
			String startDay = tanggal.format(calStart.getTime());
			String endDay = tanggal.format(calEnd.getTime());

			List<HistoryBank> data = historyBankRepo.setorRekap(startDate, endDate);
			Long jumlah = historyBankRepo.jumlahSetorRekap(startDate, endDate);
			
			Long total = historyBankRepo.totalSetorRekap(startDate, endDate);
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(total);
			
			ByteArrayOutputStream historySetorPdf = ExportToPdfSetor(data, jumlah, numberFormat.format(currencyNominal.getValue()).toString(), "Periode "+startDay+" - "+endDay);
			ByteArrayOutputStream historySetorXls = WriteExcelToEmail(data);
			
			Context ctx = new Context();
			ctx.setVariable("hari", startDay+" - "+endDay);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			transaksiNasabahService.sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai "+startDay+" - "+endDay, 
					"History Setor " + startDay+" - "+endDay + ".pdf", "SetorMingguan", ctx, historySetorPdf);
			transaksiNasabahService.sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai "+startDay+" - "+endDay, 
					"History Setor " + startDay+" - "+endDay + ".xls", "SetorMingguan", ctx, historySetorXls);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//	Setiap tanggal 1 jam 7
	@Scheduled(cron = "* * * * * *")
	public void historyNotificationMonthly() throws MessagingException, IOException, DocumentException {
		try {
			Date harini = new Date();
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.MONTH, -1);
	        
	        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM");
	        String startDate = date.format(cal.getTime());
	        String endDate = date.format(harini);
	        String start = startDate+"-01";
	        String end = endDate+"-01";
			
			SimpleDateFormat tanggal = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
			String bulan = tanggal.format(cal.getTime());

			List<HistoryBank> data = historyBankRepo.setorRekap(start, end);
			Long jumlah = historyBankRepo.jumlahSetorRekap(start, end);
			
			Long total = historyBankRepo.totalSetorRekap(start, end);
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(total);
			
			ByteArrayOutputStream historySetorPdf = ExportToPdfSetor(data, jumlah, numberFormat.format(currencyNominal.getValue()).toString(), "Periode Bulan "+bulan);
			ByteArrayOutputStream historySetorXls = WriteExcelToEmail(data);
			
			Context ctx = new Context();
			ctx.setVariable("bulan", bulan);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			transaksiNasabahService.sendEmail("maulanairzan5@gmail.com", "Laporan Transaksi Setor Tunai Bulan "+bulan, 
					"History Setor " + bulan + ".pdf", "SetorBulanan", ctx, historySetorPdf);
			transaksiNasabahService.sendEmail("maulanairzan5@gmail.com", "Laporan Transaksi Setor Tunai Bulan "+bulan,
					"History Setor " + bulan + ".xls", "SetorBulanan", ctx, historySetorXls);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}

	public ByteArrayOutputStream ExportToPdfSetor(List<HistoryBank> data, Long jumlah, String total, String subJudul) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, outputStream);
		pdfDoc.open();

		Paragraph title = new Paragraph("Laporan Transaksi Setor Tunai BANK XYZ",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);
		Paragraph subTitle = new Paragraph(subJudul, new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		subTitle.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(subTitle);

		// Add the generation date
		pdfDoc.add(new Paragraph(" "));
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(5);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell(Align("ID History Transaksi"));
		pdfTable.addCell(Align("Nomor Rekening"));
		pdfTable.addCell(Align("Nama Nasabah"));
		pdfTable.addCell(Align("Tanggal Transaksi"));
		pdfTable.addCell(Align("Nominal"));
		for (int i = 0; i < 5; i++) {
			pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
		}

		// Iterate through the data and add it to the table
		for (HistoryBank entity : data) {
			pdfTable.addCell(Align(String
					.valueOf(entity.getIdHistoryBank() != null ? String.valueOf(entity.getIdHistoryBank()) : "-")));
			pdfTable.addCell(Align(String.valueOf(
					entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-")));

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = "-";
			if (entity.getTanggal() != null) {
				formattedDate = formatter.format(entity.getTanggal());
			}
			pdfTable.addCell(Align(formattedDate));

			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(entity.getUang());
			pdfTable.addCell(Align(String.valueOf(numberFormat.format(currencyNominal.getValue()) != null
					? String.valueOf(numberFormat.format(currencyNominal.getValue()))
					: "-")));
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);
		pdfDoc.add(new Paragraph(""));
		pdfDoc.add(new Paragraph("Jumlah transaksi setor tunai selama "+ subJudul +" sebanyak " + jumlah+", dengan total uang masuk sebesar "+total+"."));

		pdfDoc.close();
		pdfWriter.close();

		return outputStream;
	}
	
public ByteArrayOutputStream WriteExcelToEmail(List<HistoryBank> data) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Users");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Create the header row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "Username", "Nama", "Alamat", "Email", "Telp", "Program Name", "Created Date", "Created By",
				"Updated Date", "Updated By" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Write data to the sheet
		int rowNum = 1;
		for (HistoryBank historyBank : data) {
			Row row = sheet.createRow(rowNum++);
			if(historyBank.getTanggal() == null) {
				row.createCell(0).setCellValue("");
			} else {
				row.createCell(0).setCellValue(historyBank.getTanggal().toString());
			}
			if(historyBank.getRekening().getNorek()== null) {
				row.createCell(1).setCellValue("");
			} else {
				row.createCell(1).setCellValue(historyBank.getRekening().getNorek());
			}
			if(historyBank.getStatusKet()== null) {
				row.createCell(2).setCellValue("");
			} else {
				row.createCell(2).setCellValue(historyBank.getStatusKet());
			}
			if(historyBank.getNama()== null) {
				row.createCell(3).setCellValue("");
			} else {
				row.createCell(3).setCellValue(historyBank.getNama());
			}
			if(historyBank.getUang()== null) {
				row.createCell(4).setCellValue("");
			} else {
				row.createCell(4).setCellValue(historyBank.getUang());
			}
			if(historyBank.getNoRekTujuan() == null) {
				row.createCell(5).setCellValue("");
			} else {
				row.createCell(5).setCellValue(historyBank.getNoRekTujuan());
			}
			if(historyBank.getNoTlp() == null) {
				row.createCell(6).setCellValue("");
			} else {
				row.createCell(6).setCellValue(historyBank.getNoTlp());
			}
			if(historyBank.getNamaTujuan() == null) {
				row.createCell(7).setCellValue("");
			} else {
				row.createCell(7).setCellValue(historyBank.getNamaTujuan());
			}
		}

		// Resize the columns to fit the contents
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the workbook to the output file
		workbook.write(outputStream);
	    workbook.close();
	    return outputStream;
	}
}

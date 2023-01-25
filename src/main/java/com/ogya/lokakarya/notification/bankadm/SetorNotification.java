package com.ogya.lokakarya.notification.bankadm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
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
import com.ogya.lokakarya.configuration.nasabah.LaporanSetorConfigurationProperties;
import com.ogya.lokakarya.entity.bankadm.HistoryBank;
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.repository.bankadm.HistoryBankRepository;
import com.ogya.lokakarya.service.bankadm.TransaksiNasabahService;
import com.ogya.lokakarya.util.CurrencyData;

@Service
public class SetorNotification {
	@Autowired
	private HistoryBankRepository historyBankRepo;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private LaporanSetorConfigurationProperties setorConfig;

	// Setiap hari jam 7
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

			InputStreamSource historySetorPdf = ExportToPdfSetor(data, jumlah,
					numberFormat.format(currencyNominal.getValue()).toString(), "Periode " + day);
			InputStreamSource historySetorXls = WriteExcelToEmail(data);

			Context ctx = new Context();
			ctx.setVariable("tanggal", day);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			List<String> namaFile = new ArrayList<>();
			namaFile.add("History Setor " + day + ".pdf");
			namaFile.add("History Setor " + day + ".xls");
			
			List<InputStreamSource> file = new ArrayList<>();
			file.add(historySetorPdf);
			file.add(historySetorXls);
			
			sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai " + day,
					namaFile, "SetorHarian", ctx, file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Setiap hari Senin jam 7
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

			InputStreamSource historySetorPdf = ExportToPdfSetor(data, jumlah,
					numberFormat.format(currencyNominal.getValue()).toString(), "Periode " + startDay + " - " + endDay);
			InputStreamSource historySetorXls = WriteExcelToEmail(data);

			Context ctx = new Context();
			ctx.setVariable("hari", startDay + " - " + endDay);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			List<String> namaFile = new ArrayList<>();
			namaFile.add("History Setor " + startDay + " - " + endDay + ".pdf");
			namaFile.add("History Setor " + startDay + " - " + endDay + ".xls");
			
			List<InputStreamSource> file = new ArrayList<>();
			file.add(historySetorPdf);
			file.add(historySetorXls);
			
			sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai " + startDay + " - " + endDay,
					namaFile, "SetorHarian", ctx, file);
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Setiap tanggal 1 jam 7
	@Scheduled(cron = "0 0 7 1 * *")
	public void historyNotificationMonthly() throws MessagingException, IOException, DocumentException {
		try {
			Date harini = new Date();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);

			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM");
			String startDate = date.format(cal.getTime());
			String endDate = date.format(harini);
			String start = startDate + "-01";
			String end = endDate + "-01";

			SimpleDateFormat tanggal = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
			String bulan = tanggal.format(cal.getTime());

			List<HistoryBank> data = historyBankRepo.setorRekap(start, end);
			Long jumlah = historyBankRepo.jumlahSetorRekap(start, end);

			Long total = historyBankRepo.totalSetorRekap(start, end);
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(total);

			InputStreamSource historySetorPdf = ExportToPdfSetor(data, jumlah,
					numberFormat.format(currencyNominal.getValue()).toString(), "Periode Bulan " + bulan);
			InputStreamSource historySetorXls = WriteExcelToEmail(data);

			Context ctx = new Context();
			ctx.setVariable("bulan", bulan);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			List<String> namaFile = new ArrayList<>();
			namaFile.add("History Setor " + bulan + ".pdf");
			namaFile.add("History Setor " + bulan + ".xls");
			
			List<InputStreamSource> file = new ArrayList<>();
			file.add(historySetorPdf);
			file.add(historySetorXls);
			
			sendEmail("usernamemeeting@gmail.com", "Laporan Transaksi Setor Tunai " + bulan,
					namaFile, "SetorHarian", ctx, file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendEmail(String tujuan, String subject, List<String> namaPdf, String templateName, Context context,
			List<InputStreamSource> file) {
		try {
			MimeMessage mailMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

			helper.setTo(tujuan);
			helper.setSubject(subject);
			String html = templateEngine.process(templateName, context);
			helper.setText(html, true);
			for (int i = 0; i < file.size(); i++) {
				String fileName = namaPdf.get(i);
				helper.addAttachment(fileName, file.get(i));
			}
			javaMailSender.send(mailMessage);
			System.out.println("Email send");

		} catch (MessagingException e) {
			System.err.println("Failed send email");
			e.printStackTrace();
		}
	}
	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}

	public InputStreamSource ExportToPdfSetor(List<HistoryBank> data, Long jumlah, String total, String subJudul)
			throws Exception {
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("column/columnSetor.properties");
//		Properties properties = new Properties();
//		properties.load(inputStream);
//		List<String> columnNames = new ArrayList<>(properties.stringPropertyNames());
		List<String> columnNames = setorConfig.getColumn();
		int columnLength = columnNames.size();
		
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
		PdfPTable pdfTable = new PdfPTable(columnLength);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		for (String columnName : columnNames) {
//			pdfTable.addCell(Align(properties.getProperty(columnName)));
			pdfTable.addCell(Align(columnName));
		}
		for (int i = 0; i < 5; i++) {
			pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
		}

		// Iterate through the data and add it to the table
		for (HistoryBank entity : data) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = "-";
			if (entity.getTanggal() != null) {
				formattedDate = formatter.format(entity.getTanggal());
			}
			pdfTable.addCell(Align(formattedDate));
			
			pdfTable.addCell(Align(String
					.valueOf(entity.getIdHistoryBank() != null ? String.valueOf(entity.getIdHistoryBank()) : "-")));
			pdfTable.addCell(Align(String.valueOf(
					entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-")));

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
		pdfDoc.add(new Paragraph("Jumlah transaksi setor tunai selama " + subJudul + " sebanyak " + jumlah
				+ ", dengan total uang masuk sebesar " + total + "."));

		pdfDoc.close();
		pdfWriter.close();

		byte[] bytes = outputStream.toByteArray();
		InputStreamSource attachmentSource = new ByteArrayResource(bytes);
		return attachmentSource;
	}

	public InputStreamSource WriteExcelToEmail(List<HistoryBank> data) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Setor");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Create the header row
		Row headerRow = sheet.createRow(0);
//		InputStream inputStream = getClass().getClassLoader()
//				.getResourceAsStream("column/columnSetor.properties");
//		Properties properties = new Properties();
//		properties.load(inputStream);
//		List<String> columnNames = new ArrayList<>(properties.stringPropertyNames());
		List<String> columnNames = setorConfig.getColumn();
 		
		int columnLength = columnNames.size();
		
		for (int i = 0; i < columnLength; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columnNames.get(i));
		}

		// Write data to the sheet
//		int rowNum = 1;
//		int columnNum = 0;
//		for (HistoryBank historyBank : data) {
//			Row row = sheet.createRow(rowNum++);
//			columnNum = 0;
//			for (String columnName : columnNames) {
//				String value = "-";
//				try {
//					Method method = HistoryBank.class
//							.getMethod("get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1));
//					Object result = method.invoke(historyBank);
//					value = result != null ? result.toString() : "-";
//				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//					// Handle the exception if the method is not found or cannot be invoked
//				}
//				row.createCell(columnNum).setCellValue(value);
//				columnNum++;
//			}
//			
//		}
		for (int i = 0; i < data.size(); i++) {
			Row dataRow = sheet.createRow(i + 1);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = "-";
			if (data.get(i).getTanggal() != null) {
				formattedDate = formatter.format(data.get(i).getTanggal());
			}
			dataRow.createCell(0).setCellValue(formattedDate);
			dataRow.createCell(1).setCellValue(data.get(i).getIdHistoryBank());
			dataRow.createCell(2).setCellValue(data.get(i).getRekening().getNorek());
			
			dataRow.createCell(3).setCellValue(data.get(i).getNama());
			dataRow.createCell(4).setCellValue(data.get(i).getUang());
		}

		// Resize the columns to fit the contents
		for (int i = 0; i < columnLength; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the workbook to the output file
		workbook.write(outputStream);
		workbook.close();
		
		byte[] bytes = outputStream.toByteArray();
		InputStreamSource attachmentSource = new ByteArrayResource(bytes);
		workbook.close();
		return attachmentSource;
	}
}

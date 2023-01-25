package com.ogya.lokakarya.notification.usermanagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.repository.usermanagement.UsersRepository;
import com.ogya.lokakarya.wrapper.usermanagement.NotificationWrapper;

public class LaporanPenambahanUserNotification {
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;

	private String[] receiver = { "maulanairzan5@gmail.com" };
//	private String[] cc = {"taerakim.21@gmail.com", "eonjejjeumilkka@gmail.com"};
	private String[] cc = {};

	private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
	
	@Scheduled(cron = "0 0 7 * * *") // <-- second, minute, hour, day, month
	public void DailyNotification() throws Exception {
		Date date = FindPrevDay(new Date());

		SimpleDateFormat format = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("in", "ID"));
		String day = format.format(date);

		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Penambahan User Harian");
		description.setTopHeader("Laporan Penambahan User Harian");
		description.setBotHeader("Hari : " + day);
		description.setTitlePdf("Laporan Penambahan User Harian " + day);
		description.setFileName("LaporanPenambahanUserHarian(" + day + ")");

		List<Users> dailyData = usersRepository.newUsersDaily();
		List<InputStreamSource> attachments = new ArrayList<>();
		List<String> attachmentsName = new ArrayList<>();
		attachments.add(ExportToPdfNotification(dailyData, description));
		attachmentsName.add(description.getFileName()+".pdf");
		attachments.add(WriteExcelToEmail(dailyData, description));
		attachmentsName.add(description.getFileName()+".xls");
		SendEmailWithAttachment(attachments,attachmentsName,description);
		
	}

	@Scheduled(cron = "0 0 7 1 * *") // <-- second, minute, hour, day, month
	public void MonthlyNotification() throws Exception {
		Date date = FindPrevDay(new Date());

		SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
		String month = format.format(date);

		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Penambahan User Bulanan");
		description.setTopHeader("Laporan Penambahan User Bulanan");
		description.setBotHeader("Bulan : " + month);
		description.setTitlePdf("Laporan Penambahan User Bulanan(" + month + ")");
		description.setFileName("LaporanPenambahanUserBulanan(" + month + ")");

		List<Users> monthlyData = usersRepository.newUsersMonthly();
		List<InputStreamSource> attachments = new ArrayList<>();
		List<String> attachmentsName = new ArrayList<>();
		attachments.add(ExportToPdfNotification(monthlyData, description));
		attachmentsName.add(description.getFileName()+".pdf");
		attachments.add(WriteExcelToEmail(monthlyData, description));
		attachmentsName.add(description.getFileName()+".xls");
		SendEmailWithAttachment(attachments,attachmentsName,description);
	}

	@Scheduled(cron = "0 0 7 * * MON") // <-- second, minute, hour, day, month
	public void WeeklyNotification() throws Exception {
		Date date = FindPrevDay(new Date());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		SimpleDateFormat format = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("in", "ID"));
		String weekEnd = format.format(date);

		Calendar calendarWeekStart = new GregorianCalendar();
		calendarWeekStart.setTime(date);
		calendarWeekStart.add(Calendar.DATE, -6);
		Date dateStart = calendarWeekStart.getTime();
		String weekStart = format.format(dateStart);

		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Penambahan User Mingguan");
		description.setTopHeader("Laporan Penambahan User Mingguan");
		description.setBotHeader("Hari : " + weekStart + " - " + weekEnd);
		description.setTitlePdf("Laporan Penambahan User Mingguan(" + weekStart + " - " + weekEnd + ")");
		description.setFileName("LaporanPenambahanUserMingguan(" + weekStart + " - " + weekEnd + ")");

		List<Users> weeklyData = usersRepository.newUsersWeekly();
		List<InputStreamSource> attachments = new ArrayList<>();
		List<String> attachmentsName = new ArrayList<>();
		attachments.add(ExportToPdfNotification(weeklyData, description));
		attachmentsName.add(description.getFileName()+".pdf");
		attachments.add(WriteExcelToEmail(weeklyData, description));
		attachmentsName.add(description.getFileName()+".xls");
		SendEmailWithAttachment(attachments,attachmentsName,description);
	}

	private static Date FindPrevDay(Date date) {
		return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}
	
	public void SendEmailWithAttachment(List<InputStreamSource> data, List<String> fileNames, NotificationWrapper description) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setTo(description.getReceiver());
			mimeMessageHelper.setCc(description.getCc());
			mimeMessageHelper.setSubject(description.getSubject());

			StringBuffer buffer = new StringBuffer();
			buffer.append("<h3>" + description.getTopHeader() + "</h3>");
			buffer.append("<h5>" + description.getBotHeader() + "</h5>");
			String body = buffer.toString();
			mimeMessageHelper.setText(body, true);

//			mimeMessageHelper.addAttachment(description.getFileName() + dataType, data);
			for (int i =0;i<data.size(); i++) {
			    String fileName = fileNames.get(i);
			    mimeMessageHelper.addAttachment(fileName, data.get(i));
			}
			javaMailSender.send(mimeMessage);
			System.out.println("Email sent");
		} catch (MessagingException e) {
			System.err.print("Failed send email");
			e.printStackTrace();
		}
	}
	
	public InputStreamSource ExportToPdfNotification(List<Users> data, NotificationWrapper description) throws Exception {
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A4.rotate());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, baos);
		pdfDoc.open();

		Paragraph title = new Paragraph(description.getTitlePdf(), new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		// Add the generation date
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(10);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("Username");
		pdfTable.addCell("Nama");
		pdfTable.addCell("Alamat");
		pdfTable.addCell("Email");
		pdfTable.addCell("Telp");
		pdfTable.addCell("Program Name");
		pdfTable.addCell("Created Date");
		pdfTable.addCell("Created By");
		pdfTable.addCell("Updated Date");
		pdfTable.addCell("Updated By");
		BaseColor color = new BaseColor(135, 206, 235);
		for (int i = 0; i < 10; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		// Iterate through the data and add it to the table
		for (Users entity : data) {
			pdfTable.addCell(String.valueOf(entity.getUsername() != null ? String.valueOf(entity.getUsername()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getEmail() != null ? String.valueOf(entity.getEmail()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getTelp() != null ? String.valueOf(entity.getTelp()) : "-"));
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
		byte[] bytes = baos.toByteArray();
		InputStreamSource attachmentSource = new ByteArrayResource(bytes);
		return attachmentSource;
//		SendEmailWithAttachment(attachmentSource, description, ".pdf");

	}
	
	public InputStreamSource WriteExcelToEmail(List<Users> data, NotificationWrapper description) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Users");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

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
		for (Users user : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(user.getUsername());
			row.createCell(1).setCellValue(user.getNama());
			row.createCell(2).setCellValue(user.getAlamat());
			row.createCell(3).setCellValue(user.getEmail());
			row.createCell(4).setCellValue(user.getTelp());
			if(user.getProgramName() == null) {
				row.createCell(5).setCellValue("");
			} else {
				row.createCell(5).setCellValue(user.getProgramName());
			}
			
			if(user.getCreatedDate() == null) {
				row.createCell(6).setCellValue("");
			} else {
				row.createCell(6).setCellValue(user.getCreatedDate().toString());
			}
			
			if(user.getCreatedBy() == null) {
				row.createCell(7).setCellValue("");
			} else {
				row.createCell(7).setCellValue(user.getCreatedBy());
			}
			
			if(user.getUpdatedDate() == null) {
				row.createCell(8).setCellValue("");
			} else {
				row.createCell(8).setCellValue(user.getUpdatedDate().toString());
			}
			
			
			if(user.getUpdatedBy() == null) {
				row.createCell(9).setCellValue("");
			} else {
				row.createCell(9).setCellValue(user.getUpdatedBy());
			}
		}

		// Resize the columns to fit the contents
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the workbook to the output file
		workbook.write(baos);
		baos.flush();
		baos.close();
		byte[] bytes = baos.toByteArray();
		InputStreamSource attachmentSource = new ByteArrayResource(bytes);
//		SendEmailWithAttachment(attachmentSource, description, ".xls");
	    workbook.close();
	    return attachmentSource;
	}
}

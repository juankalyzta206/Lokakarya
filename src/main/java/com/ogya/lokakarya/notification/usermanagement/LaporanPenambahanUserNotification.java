package com.ogya.lokakarya.notification.usermanagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.notification.usermanagement.column.UsersNotificationColumn;
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
	
	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	
	public InputStreamSource ExportToPdfNotification(List<Users> data, NotificationWrapper description) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("column/columnUsermanagement.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		List<String> columnNames = new ArrayList<>(properties.stringPropertyNames());
		int columnLength = columnNames.size();
		
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
				PdfPTable pdfTable = new PdfPTable(columnLength);

				pdfTable.setWidthPercentage(100);
				pdfTable.setSpacingBefore(10f);
				pdfTable.setSpacingAfter(10f);

				 for (String columnName : columnNames) {
				        pdfTable.addCell(Align(properties.getProperty(columnName)));
				    }
				BaseColor color = new BaseColor(135, 206, 235);
				for (int i = 0; i < columnLength; i++) {
					pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
				}

				// Iterate through the data and add it to the table
				 for (Users entity : data) {
				        for (String columnName : columnNames) {
				            String value = "";
				            if (columnName.equals("username")) {
				                value = String.valueOf(entity.getUsername() != null ? String.valueOf(entity.getUsername()) : "-");
				            } else if (columnName.equals("nama")) {
				                value = String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-");
				            } else if (columnName.equals("alamat")) {
				                value = String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-");
				            } else if (columnName.equals("telp")) {
				                value = String.valueOf(entity.getTelp() != null ? String.valueOf(entity.getTelp()) : "-");
				            } else if (columnName.equals("email")) {
				                value = String.valueOf(entity.getEmail() != null ? String.valueOf(entity.getEmail()) : "-");
				            } else if (columnName.equals("programName")) {
				                value = String.valueOf(entity.getProgramName() != null ? String.valueOf(entity.getProgramName()) : "-");
				            } else if (columnName.equals("createdDate")) {
				                value = String.valueOf(entity.getCreatedDate() != null ? String.valueOf(entity.getCreatedDate()) : "-");
				            } else if (columnName.equals("createdBy")) {
				                value = String.valueOf(entity.getCreatedBy() != null ? String.valueOf(entity.getCreatedBy()) : "-");
				            } else if (columnName.equals("updatedDate")) {
				                value = String.valueOf(entity.getUpdatedDate() != null ? String.valueOf(entity.getUpdatedDate()) : "-");
				            } else if (columnName.equals("updatedBy")) {
				                value = String.valueOf(entity.getUpdatedBy() != null ? String.valueOf(entity.getUpdatedBy()) : "-");
				            }
				            pdfTable.addCell(Align(value));
				        }
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
		String[] headers = UsersNotificationColumn.getHeaders();
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Write data to the sheet
		int rowNum = 1;
		for (Users entity : data) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(String.valueOf(entity.getUsername() != null ? String.valueOf(entity.getUsername()) : "-"));
			row.createCell(1).setCellValue(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
			row.createCell(2).setCellValue(String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-"));
			row.createCell(3).setCellValue(String.valueOf(entity.getEmail() != null ? String.valueOf(entity.getEmail()) : "-"));
			row.createCell(4).setCellValue(String.valueOf(entity.getTelp() != null ? String.valueOf(entity.getTelp()) : "-"));
			row.createCell(5).setCellValue(String.valueOf(entity.getProgramName() != null ? String.valueOf(entity.getProgramName()) : "-"));
			row.createCell(6).setCellValue(String.valueOf(entity.getCreatedDate() != null ? String.valueOf(entity.getCreatedDate()) : "-"));
			row.createCell(7).setCellValue(String.valueOf(entity.getCreatedBy() != null ? String.valueOf(entity.getCreatedBy()) : "-"));
			row.createCell(8).setCellValue(String.valueOf(entity.getUpdatedDate() != null ? String.valueOf(entity.getUpdatedDate()) : "-"));
			row.createCell(9).setCellValue(String.valueOf(entity.getUpdatedBy() != null ? String.valueOf(entity.getUpdatedBy()) : "-"));
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
	    workbook.close();
	    return attachmentSource;
	}
}

package com.ogya.lokakarya.notification.bankadm;

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.ogya.lokakarya.entity.bankadm.HistoryBank;
import com.ogya.lokakarya.repository.bankadm.HistoryBankRepository;
import com.ogya.lokakarya.wrapper.usermanagement.NotificationWrapper;

@Service
@Transactional
public class BankAdminTarikNotification {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	HistoryBankRepository historyBankRepo;

	private String[] receiver = { "1811500071@student.budiluhur.ac.id" };
	private String[] cc = { "taerakim.21@gmail.com", "eonjejjeumilkka@gmail.com", "maulanairzan5@gmail.com" };

	private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	@Scheduled(cron = "0 0 7 * * *") // <-- second, minute, hour, day, month
	public void DailyNotification() throws Exception {
		Date date = new Date();
		date = FindPrevDay(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Locale id = new Locale("in", "ID");
		SimpleDateFormat f = new SimpleDateFormat("EEEE", id);
		String dayName = f.format(date);
		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Tarik Tunai");
		description.setTopHeader("Laporan Tarik Tunai Harian");
		description.setBotHeader("Hari : " + dayName + ", " + day + "/" + month + "/" + year);
		description.setTitlePdf(
				"Laporan Tarik Tunai Harian(" + dayName + ", " + day + " " + getMonthForInt(month) + " " + year + ")");
		description.setFileName("LaporanTarikTunaiHarian(" + day + "/" + month + "/" + year + ")");
		List<HistoryBank> dailyData = historyBankRepo.newTarikDaily();
		ExportToPdfNotification(dailyData, description);
		ExportToExcelNotification(dailyData, description);

	}

	@Scheduled(cron = "0 0 7 1 * *") // <-- second, minute, hour, day, month
	public void MonthlyNotification() throws Exception {
		Date date = new Date();
		date = FindPrevDay(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Tarik Tunai");
		description.setTopHeader("Laporan Tarik Tunai Bulanan");
		description.setBotHeader("Bulan " + getMonthForInt(month) + " tahun " + year);
		description.setTitlePdf("Laporan Tarik Tunai Bulanan(" + getMonthForInt(month) + " " + year + ")");
		description.setFileName("LaporanTarikTunaiBulanan(" + month + "/" + year + ")");
		List<HistoryBank> monthlyData = historyBankRepo.newTarikMonthly();
		ExportToPdfNotification(monthlyData, description);
		ExportToExcelNotification(monthlyData, description);
	}

	@Scheduled(cron = "0 0 7 * * MON") // <-- second, minute, hour, day, month
	public void WeeklyNotification() throws Exception {
		Date date = new Date();
		date = FindPrevDay(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		Calendar calendarStartWeek = new GregorianCalendar();
		calendarStartWeek.setTime(date);
		calendarStartWeek.add(Calendar.DATE, -6);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int yearStartWeek = calendarStartWeek.get(Calendar.YEAR);
		int monthStartWeek = calendarStartWeek.get(Calendar.MONTH) + 1;
		int dayStartWeek = calendarStartWeek.get(Calendar.DAY_OF_MONTH);
		Locale id = new Locale("in", "ID");
		SimpleDateFormat f = new SimpleDateFormat("EEEE", id);
		String dayName = f.format(date);
		String dayNameStartWeek = f.format(FindStartWeek(date));
		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Tarik Tunai");
		description.setTopHeader("Laporan Tarik Tunai Mingguan");
		description.setBotHeader("Hari : " + dayNameStartWeek + ", " + dayStartWeek + "/" + monthStartWeek + "/"
				+ yearStartWeek + "-" + dayName + ", " + day + "/" + month + "/" + year);
		description.setTitlePdf("Laporan Tarik Tunai Mingguan(" + dayNameStartWeek + ", " + dayStartWeek + "/"
				+ monthStartWeek + "/" + yearStartWeek + "-" + dayName + ", " + day + "/" + month + "/" + year + ")");
		description.setFileName("LaporanTarik unaiMingguan(" + dayNameStartWeek + ", " + dayStartWeek + "/"
				+ monthStartWeek + "/" + yearStartWeek + "-" + dayName + ", " + day + "/" + month + "/" + year + ")");
		List<HistoryBank> weeklyData = historyBankRepo.newTarikWeekly();
		ExportToPdfNotification(weeklyData, description);
		ExportToExcelNotification(weeklyData, description);
	}

	String getMonthForInt(int num) {
		num = num - 1;
		String month = "wrong";
		Locale id = new Locale("in", "ID");
		DateFormatSymbols dfs = new DateFormatSymbols(id);
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 12) {
			month = months[num];
		}
		return month;
	}

	private static Date FindPrevDay(Date date) {
		return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}

	private static Date FindStartWeek(Date date) {
		return new Date(date.getTime() - 6 * MILLIS_IN_A_DAY);
	}

	public void SendEmailWithAttachment(InputStreamSource pdfData, InputStreamSource xlsData,
			NotificationWrapper description) {

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

			mimeMessageHelper.addAttachment(description.getFileName() + ".pdf", pdfData);
			mimeMessageHelper.addAttachment(description.getFileName() + ".xls", xlsData);
			javaMailSender.send(mimeMessage);
			System.out.println("Email sent");
		} catch (MessagingException e) {
			System.err.print("Failed send email");
			e.printStackTrace();
		}
	}

	public void ExportToPdfNotification(List<HistoryBank> data, NotificationWrapper description) throws Exception {
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
		PdfPTable pdfTable = new PdfPTable(5);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell(Align("Nomor Rekening"));
		pdfTable.addCell(Align("Nama Nasabah"));
		pdfTable.addCell(Align("Tanggal Transaksi"));
		pdfTable.addCell(Align("Nominal"));
		pdfTable.addCell(Align("Keterangan"));
		for (int i = 0; i < 5; i++) {
			pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
		}

		// Iterate through the data and add it to the table
		for (HistoryBank entity : data) {
			pdfTable.addCell(Align(String.valueOf(
					entity.getRekening().getNorek() != null ? String.valueOf(entity.getRekening().getNorek()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-")));

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = "-";
			if (entity.getTanggal() != null) {
				formattedDate = formatter.format(entity.getTanggal());
			}
			pdfTable.addCell(Align(formattedDate));
			pdfTable.addCell(Align(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-")));
			pdfTable.addCell(Align("Tarik Tunai"));
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();
		byte[] bytes = baos.toByteArray();
		InputStreamSource attachmentSource = new ByteArrayResource(bytes);
		// SendEmailWithAttachmentPDF(attachmentSource, description);
		SendEmailWithAttachment(attachmentSource, attachmentSource, description);
	}

	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}

	public void ExportToExcelNotification(List<HistoryBank> data, NotificationWrapper description) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Transactions");

		// Create the header row
		XSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Nomor Rekening");
		headerRow.createCell(1).setCellValue("Nama Nasabah");
		headerRow.createCell(2).setCellValue("Tanggal Transaksi");
		headerRow.createCell(3).setCellValue("Nominal");
		headerRow.createCell(4).setCellValue("Keterangan");

		// Iterate through the data and add it to the sheet
		for (int i = 0; i < data.size(); i++) {
			XSSFRow row = sheet.createRow(i + 1);
			HistoryBank entity = data.get(i);
			row.createCell(0).setCellValue(entity.getRekening().getNorek());
			row.createCell(1).setCellValue(entity.getNama());

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = "-";
			if (entity.getTanggal() != null) {
				formattedDate = formatter.format(entity.getTanggal());
			}
			row.createCell(2).setCellValue(formattedDate);
			row.createCell(3).setCellValue(entity.getUang());
			row.createCell(4).setCellValue("Tarik Tunai");
		}

		// Autosize the columns
		for (int i = 0; i < 5; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the workbook to a file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		workbook.write(baos);
		byte[] bytes = baos.toByteArray();
		InputStreamSource attachmentSource = new ByteArrayResource(bytes);
		workbook.close();
		description.setFileName(description.getFileName() + ".xlsx");
		// SendEmailWithAttachmentXLS(attachmentSource, description);
		SendEmailWithAttachment(attachmentSource, attachmentSource, description);

	}

}
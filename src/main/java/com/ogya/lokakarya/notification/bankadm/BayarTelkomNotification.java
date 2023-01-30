package com.ogya.lokakarya.notification.bankadm;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.ogya.lokakarya.configuration.nasabah.LaporanBayarTelponConfigurationProperties;
import com.ogya.lokakarya.entity.bankadm.HistoryBank;
import com.ogya.lokakarya.entity.bankadm.MasterBank;
import com.ogya.lokakarya.repository.bankadm.HistoryBankRepository;

@Service
@Transactional
public class BayarTelkomNotification {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	HistoryBankRepository historyBankRepository;
	@Autowired
	LaporanBayarTelponConfigurationProperties bayarTelponConfigurationProperties;
	@Value("${cron.daily}")
	private String dailyCron;
	
	@Value("${cron.weekly}")
	private String weeklyCron;

	@Value("${cron.monthly}")
	private String monthlyCron;

//	===========================================KirimEmail=====================================================
//	Setiap Hari Jam 7
	@Scheduled(cron = "${cron.daily}")
	public void sendEmailDay() throws Exception {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date tanggal = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String yesterday = dateFormat.format(tanggal);

		SimpleDateFormat format = new SimpleDateFormat("EEEE dd/MM/yyyy", new Locale("in", "ID"));
		String dateString = format.format(tanggal);

		List<HistoryBank> data = historyBankRepository.laporanBayarTeleponToday(yesterday);
		String fileName = "Laporan Transaksi Bank Bayar Telepon Hari " + dateString;
		
		helper.setTo("usernamemeeting@gmail.com");
		helper.setCc("taerakim.21@gmail.com");
		helper.setSubject(fileName);
		helper.setText(fileName, true);

		ByteArrayOutputStream pdf = ExportToPdf(data, fileName);
		ByteArrayOutputStream xlsx = exportToXLSBayarTelpon(data, fileName);
		helper.addAttachment(fileName + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		helper.addAttachment(fileName + ".xlsx", new ByteArrayResource(xlsx.toByteArray()));

		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	Setiap Hari Senin Jam 7
	@Scheduled(cron = "${cron.weekly}")
	public void sendEmailWeek() throws Exception {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

		Calendar calStart = Calendar.getInstance();
		calStart.add(Calendar.DATE, -7);
		Date dateStart = calStart.getTime();

		Calendar calEnd = Calendar.getInstance();
		calEnd.add(Calendar.DATE, -1);
		Date dateEnd = calEnd.getTime();

		int weekOfMonth = calStart.get(Calendar.WEEK_OF_MONTH);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String start = dateFormat.format(dateStart);
		String end = dateFormat.format(dateEnd);

		SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
		String bulan = format.format(dateStart);

		List<HistoryBank> data = historyBankRepository.bayarTeleponRekap(start, end);
		String fileName = "Laporan Bayar Telepon Minggu ke-" + weekOfMonth + " " + bulan;

		helper.setTo("usernamemeeting@gmail.com");
		helper.setCc("taerakim.21@gmail.com");
		helper.setSubject(fileName);
		helper.setText(fileName, true);

		ByteArrayOutputStream pdf = ExportToPdf(data, fileName);
		ByteArrayOutputStream xlsx = exportToXLSBayarTelpon(data, fileName);
		helper.addAttachment(fileName + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		helper.addAttachment(fileName + ".xlsx", new ByteArrayResource(xlsx.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	Setiap tanggal 1 jam 7
	@Scheduled(cron = "${cron.monthly}")
	public void sendEmailMonth() throws Exception {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

		Date harini = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String startDate = dateFormat.format(cal.getTime());
		String endDate = dateFormat.format(harini);
		String start = startDate + "-01";
		String end = endDate + "-01";

		SimpleDateFormat tanggal = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
		String bulan = tanggal.format(cal.getTime());

		List<HistoryBank> data = historyBankRepository.bayarTeleponRekap(start, end);
		String fileName = "Laporan Bayar Telepon Bulan " + bulan;

		helper.setTo("usernamemeeting@gmail.com");
		helper.setCc("taerakim.21@gmail.com");
		helper.setSubject(fileName);
		helper.setText(fileName, true);

		ByteArrayOutputStream pdf = ExportToPdf(data, fileName);
		ByteArrayOutputStream xlsx = exportToXLSBayarTelpon(data, fileName);
		helper.addAttachment(fileName + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		helper.addAttachment(fileName + ".xlsx", new ByteArrayResource(xlsx.toByteArray()));

		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	======================================DynamicColumn==================================================

	public boolean containsChar(String s, char search) {
		if (s.length() == 0)
			return false;
		else
			return s.charAt(0) == search || containsChar(s.substring(1), search);
	}

	public ByteArrayOutputStream exportToXLSBayarTelpon(List<HistoryBank> data, String fileName) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(fileName);

		List<String> columnNames = bayarTelponConfigurationProperties.getTelpon();
		int columnLength = columnNames.size();

		XSSFRow headerRow = sheet.createRow(0);

		for (int i = 0; i < columnLength; i++) {
			XSSFCell headerCell = headerRow.createCell(i);
			if (columnNames.get(i).equals("Rekening:Norek")) {
				headerCell.setCellValue("REKENING");
			} else if (columnNames.get(i).equals("Uang")) {
				headerCell.setCellValue("NOMINAL");
			} else {
				headerCell.setCellValue(columnNames.get(i).toUpperCase());
			}
		}
		
		int rowNum = 1;
		int columnNum = 0;
		for (HistoryBank entity : data) {
			Row row = sheet.createRow(rowNum++);
			columnNum = 0;
			for (String columnName : columnNames) {
				String value = "-";
				try {
					String columnNameNoSpace = columnName.replaceAll("\\s", "");
					Boolean isForeignKey = containsChar(columnNameNoSpace, ':');
					String[] foreignClass = columnNameNoSpace.split(":", 2);
					if (!isForeignKey) {
						Method method = HistoryBank.class.getMethod("get" + columnNameNoSpace);
						Object result = method.invoke(entity);
						value = result != null ? result.toString() : "-";
					} else {
						Method method = HistoryBank.class.getMethod("get" + foreignClass[0]);
						if (foreignClass[0].equals("Rekening")) {
							Method masterBankMethod = MasterBank.class.getMethod("get" + foreignClass[1]);
							Object result = masterBankMethod.invoke(method.invoke(entity));
							value = result != null ? result.toString() : "-";
						}
					}
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					// Handle the exception if the method is not found or cannot be invoked
				}
				row.createCell(columnNum).setCellValue(value);
				columnNum++;
			}
		}

		/* Resize the columns to fit the contents */
		for (int i = 0; i < columnLength; i++) {
			sheet.autoSizeColumn(i);
		}

		workbook.write(outputStream);
		workbook.close();
		return outputStream;
	}

	public void downloadXlsBayarTelpon(HttpServletResponse response) throws Exception {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=Laporan Bayar Telepon.xlsx");
		List<HistoryBank> data = historyBankRepository.laporanBayarTelepon();
		String fileName = "Laporan Bayar Telepon";
		ByteArrayOutputStream baos = exportToXLSBayarTelpon(data, fileName);
		response.setContentLength(baos.size());
		OutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
	}

	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}

	public ByteArrayOutputStream ExportToPdf(List<HistoryBank> data, String fileName) throws Exception {
		List<String> columnNames = bayarTelponConfigurationProperties.getTelpon();
		int columnLength = columnNames.size();

		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A4.rotate());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, baos);
		pdfDoc.open();

		Paragraph title = new Paragraph(fileName, new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
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
			if (columnName.equals("Uang")) {
				pdfTable.addCell(Align("NOMINAL"));
			} else if (columnName.equals("Rekening:Norek")) {
				pdfTable.addCell(Align("REKENING"));
			} else {
				pdfTable.addCell(Align(columnName.toUpperCase()));
			}
		}
		BaseColor color = new BaseColor(135, 206, 235);
		for (int i = 0; i < columnLength; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		for (HistoryBank entity : data) {
			for (String columnName : columnNames) {
				String value = "-";
				try {
					String columnNameNoSpace = columnName.replaceAll("\\s", "");
					Boolean isForeignKey = containsChar(columnNameNoSpace, ':');
					String[] foreignClass = columnNameNoSpace.split(":", 2);
					if (!isForeignKey) {
						Method method = HistoryBank.class.getMethod("get" + columnNameNoSpace);
						Object result = method.invoke(entity);
						value = result != null ? result.toString() : "-";
					} else {
						Method method = HistoryBank.class.getMethod("get" + foreignClass[0]);
						if (foreignClass[0].equals("Rekening")) {
							Method masterBankMethod = MasterBank.class.getMethod("get" + foreignClass[1]);
							Object result = masterBankMethod.invoke(method.invoke(entity));
							value = result != null ? result.toString() : "-";
						}

					}

				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					/* Handle the exception if the method is not found or cannot be invoked */
				}
				pdfTable.addCell(Align(value));
			}
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();
		return baos;

	}

	public void ExportToPdfBayarTelepon(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=Bayar Telepon.pdf");
		List<HistoryBank> data = historyBankRepository.laporanBayarTelepon();
		String title = "Laporan Bayar Telepon";
		ByteArrayOutputStream baos = ExportToPdf(data, title);
		response.setContentLength(baos.size());
		OutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
	}
}

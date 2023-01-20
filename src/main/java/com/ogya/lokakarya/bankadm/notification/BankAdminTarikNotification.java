package com.ogya.lokakarya.bankadm.notification;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

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
import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
@Service
@Transactional
public class BankAdminTarikNotification {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	HistoryBankRepository historyBankRepo;
	
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
		List<HistoryBank> dailyData = historyBankRepo.newTarikDaily(year,month,day);
		ExportToPdfNotification(dailyData, "List Created Withdrawal Daily");
	}
	
	@Scheduled(cron = "0 0 7 1 * *") // <-- second, minute, hour, day, month
	public void MonthlyNotification() throws Exception {
		Date date = new Date();
		date = FindPrevDay(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		List<HistoryBank> dailyData = historyBankRepo.newTarikMonthly(year,month);
		ExportToPdfNotification(dailyData, "List Created Withdrawal Monthly");
	}
	
	
	private static Date FindPrevDay(Date date)
	{
	  return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}
	
	
	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	
	public void ExportToPdfNotification(List<HistoryBank> data, String titleName) throws Exception{
		 // Now create a new iText PDF document
	    Document pdfDoc = new Document(PageSize.A4.rotate());
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, baos);
	    pdfDoc.open();
	    
	    Paragraph title = new Paragraph(titleName,
	            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);
	    
	    // Add the generation date
	    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

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
	    SendEmailWithAttachment(attachmentSource);
	    
	}
	
	public void SendEmailWithAttachment(InputStreamSource data) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom("irzan@maulana.com");
			mimeMessageHelper.setTo("1811500071@student.budiluhur.ac.id");
			mimeMessageHelper.addCc("maulanairzan5@gmail.com");
			mimeMessageHelper.setSubject("Test Subject");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("<h3>Laporan Transaksi Penarikan</h3>");
			buffer.append("<h5>Berikut Lampiran Laporan Transaksi</h5>");
			String body = buffer.toString();
			mimeMessageHelper.setText(body, true);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String formattedDate = dateFormat.format(date);
			String attachmentName = "Laporan("+formattedDate+").pdf";
			mimeMessageHelper.addAttachment(attachmentName, data);
			javaMailSender.send(mimeMessage);
			System.out.println("Email sent");
		} catch (MessagingException e){
			System.err.print("Failed send email");
			e.printStackTrace();
		}
	}

}

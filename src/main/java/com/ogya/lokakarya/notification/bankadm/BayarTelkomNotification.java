package com.ogya.lokakarya.notification.bankadm;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.entity.bankadm.HistoryBank;
import com.ogya.lokakarya.repository.bankadm.HistoryBankRepository;
import com.ogya.lokakarya.service.bankadm.HistoryBankService;

@Service
@Transactional
public class BayarTelkomNotification {
	@Autowired
	HistoryBankService historyBankService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	HistoryBankRepository historyBankRepository;

//	===========================================KirimEmail=====================================================
//	Setiap Hari Jam 7
	@Scheduled(cron = "0 0 7 * * *")
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
		helper.setSubject("Laporan Bayar Telepon Hari " + dateString);
		helper.setText("Laporan Bayar Hari " + dateString, true);

		ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(data, fileName);
		helper.addAttachment(fileName + ".pdf", new ByteArrayResource(pdf.toByteArray()));

		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	Setiap Hari Senin Jam 7
	@Scheduled(cron = "0 0 7 * * MON")
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
		String title = "Laporan Bayar Telepon Minggu ke-" + weekOfMonth + " " + bulan;

		helper.setTo("usernamemeeting@gmail.com");
		helper.setCc("taerakim.21@gmail.com");
		helper.setSubject("Laporan Bayar Telepon Minggu ke-" + weekOfMonth + " " + bulan);
		helper.setText("Laporan Bayar Telepon Minggu ke-" + weekOfMonth + " " + bulan, true);

		ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(data, title);
		helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	Setiap tanggal 1 jam 7
	@Scheduled(cron = "0 0 7 1 * *")
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
		String title = "Laporan Bayar Telepon Bulan " + bulan;

		helper.setTo("usernamemeeting@gmail.com");
		helper.setCc("taerakim.21@gmail.com");
		helper.setSubject("Laporan Bayar Telepon Bulan " + bulan);
		helper.setText("Laporan Bayar Telepon Bulan " + bulan, true);

		ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(data, title);
		helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));

		ByteArrayOutputStream xlsx = historyBankService.exportToXLSBayarTelpon(data, title);
		helper.addAttachment(title + ".xlsx", new ByteArrayResource(xlsx.toByteArray()));

		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	======================================DynamicColumn==================================================
	List<String> getHeaderRow() {
		List<String> headerRow = new ArrayList<String>();
		headerRow.add("Nomor Rekening");
		headerRow.add("Nama Nasabah");
		headerRow.add("Tanggal Transaksi");
		headerRow.add("Nominal");
		headerRow.add("No. Telepon");
		headerRow.add("Keterangan");
		return headerRow;
	}
	
//	List<HistoryBank> getDataBayarTelpon(){
//		List<HistoryBank> dataHistory = historyBankRepository.laporanBayarTelepon();
//		List<HistoryBank> data = new ArrayList<HistoryBank>();
//		
//		for (int i = 0; i < dataHistory.size(); i++) {
//			data.addAll(dataHistory.get(i).getRekening().getNorek());
//			
//		}
//	}

	public ByteArrayOutputStream exportToXLSBayarTelpon() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Laporan Bayar Telepon");

		List<HistoryBank> data = historyBankRepository.laporanBayarTelepon();
		List<String> header = getHeaderRow();

		StringBuilder sbHeader = new StringBuilder();
		XSSFRow headerRow = sheet.createRow(0);
		XSSFCell cell = headerRow.createCell(0);
		for (int i = 0; i < header.size(); i++) {		
			sbHeader.append(header.get(i).toString()).append(",");
			cell.setCellValue(sbHeader.toString());
		}
		
		StringBuilder sbData = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			XSSFRow dataRow = sheet.createRow(i+1);
			
			sbData.append(data.get(i).getRekening().getNorek().toString()).append(",");
//			dataRow.createCell(0).setCellValue(data.get(i).getRekening().getNorek());
//			dataRow.createCell(1).setCellValue(data.get(i).getNama());
//			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//			String formattedDate = "-";
//			if (data.get(i).getTanggal() != null) {
//				formattedDate = formatter.format(data.get(i).getTanggal());
//			}
//			dataRow.createCell(2).setCellValue(formattedDate);
//			dataRow.createCell(3).setCellValue(data.get(i).getUang());
//			dataRow.createCell(4).setCellValue(data.get(i).getNoTlp());
//			dataRow.createCell(5).setCellValue("Bayar Telepon");
			
			for (int j = 0; j < data.size(); j++) {
				XSSFCell dataCell = dataRow.createCell(data.get(i).getRekening().getNorek().SIZE);
				dataCell.setCellValue(sbData.toString());
			}
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return outputStream;

	}

	public void downloadXlsBayarTelpon(HttpServletResponse response) throws Exception {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=Laporan Bayar Telepon.xlsx");
		ByteArrayOutputStream baos = exportToXLSBayarTelpon();
		response.setContentLength(baos.size());
		OutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
	}
}

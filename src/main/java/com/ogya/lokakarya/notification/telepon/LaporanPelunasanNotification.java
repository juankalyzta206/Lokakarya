package com.ogya.lokakarya.notification.telepon;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.entity.telepon.HistoryTelkom;
import com.ogya.lokakarya.repository.telepon.HistoryTelkomRepository;
import com.ogya.lokakarya.service.telepon.HistoryService;

@Service
@Transactional
public class LaporanPelunasanNotification {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	HistoryService historyService;
	@Autowired
	HistoryTelkomRepository historyTelkomRepository;

	@Value("${cron.daily}")
	private String dailyCron;

	@Value("${cron.monthly}")
	private String monthlyCron;

	@Value("${cron.weekly}")
	private String weeklyCron;

//PDF	
//	Setiap hari jam 7
	//@Scheduled(cron = "${cron.daily}")
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

		List<HistoryTelkom> data = historyTelkomRepository.lunasDaily(yesterday);
		String title = "Laporan Pelunasan Telepon Hari " + dateString;

		helper.setTo("haha1hihi2huhu3@gmail.com");
		// helper.setCc("haha1hihi2huhu3@gmail.com");
		helper.setSubject("Laporan Bayar Telepon Hari " + dateString);
		helper.setText("Laporan Bayar Hari " + dateString, true);

		ByteArrayOutputStream pdf = historyService.ExportToPdfParam(data, title);
		helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	Setiap Hari Senin Jam 7
	//@Scheduled(cron = "${cron.weekly}")
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

		List<HistoryTelkom> data = historyTelkomRepository.lunasRekap(start, end);
		String title = "Laporan Pelunasan telpon ke-" + weekOfMonth + " " + bulan;

		helper.setTo("haha1hihi2huhu3@gmail.com");
		// helper.setCc("haha1hihi2huhu3@gmail.com");
		helper.setSubject("Laporan Bayar Telepon Minggu ke-" + weekOfMonth + " " + bulan);
		helper.setText("Laporan Pelunasan Telepon Minggu ke-" + weekOfMonth + " " + bulan, true);

		ByteArrayOutputStream pdf = historyService.ExportToPdfParam(data, title);
		helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//	Setiap tanggal 1 jam 7
	//@Scheduled(cron = "${cron.monthly}")
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

		List<HistoryTelkom> data = historyTelkomRepository.lunasRekap(start, end);
		String title = "Laporan Bayar Telepon Bulan " + bulan;

		helper.setTo("haha1hihi2huhu3@gmail.com");
		// helper.setCc("haha1hihi2huhu3@gmail.com");
		helper.setSubject("Laporan Bayar Telepon Bulan " + bulan);
		helper.setText("Laporan Bayar Telepon Bulan " + bulan, true);

		ByteArrayOutputStream pdf = historyService.ExportToPdfParam(data, title);
		helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

//EXCEL
	// setiap tanggal 1 jam 7
	//@Scheduled(cron = "${cron.monthly}")
	public void sendEmailMonthExcel() throws Exception {
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

		List<HistoryTelkom> data = historyTelkomRepository.lunasRekap(start, end);
		// List<HistoryTelkom> data = historyTelkomRepository.findAll();
		String title = "Laporan Bayar Telepon Bulan " + bulan;

		helper.setTo("haha1hihi2huhu3@gmail.com");
		// helper.setCc("haha1hihi2huhu3@gmail.com");
		helper.setSubject("Laporan Bayar Telepon Bulan " + bulan);
		helper.setText("Laporan Bayar Telepon Bulan " + bulan, true);
		InputStreamSource laporan = historyService.ExportToExcelParam(data);
		helper.addAttachment(title + ".xlsx", laporan);
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}
}

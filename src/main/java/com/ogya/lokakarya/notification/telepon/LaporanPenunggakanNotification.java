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

import com.ogya.lokakarya.configuration.telepon.LaporanPenunggakanConfigurationProperties;
import com.ogya.lokakarya.entity.telepon.TransaksiTelkom;

import com.ogya.lokakarya.repository.telepon.MasterPelangganRepository;
import com.ogya.lokakarya.repository.telepon.TransaksiTelkomRepository;
import com.ogya.lokakarya.service.telepon.TransaksiTelkomService;

@Service
@Transactional
public class LaporanPenunggakanNotification {
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	TransaksiTelkomService transaksiTelkomService;
	@Autowired
	private LaporanPenunggakanConfigurationProperties laporanPenunggakanConfigurationProperties;

	@Value("${cron.daily}")
	private String dailyCron;

	@Value("${cron.monthly}")
	private String monthlyCron;

	@Value("${cron.weekly}")
	private String weeklyCron;
//	@Value("${app.idtransaksi}")
//	private String idTransaksi;

	// Pdf
	// setiap tanggal 1 jam 7
	// @Scheduled(cron = "0 0 7 1 * *")
	//@Scheduled(cron = "${cron.monthly}")
	public void sendEmailDay() throws Exception {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date tanggal = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		String bulan = dateFormat.format(tanggal);
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");
		String tahun = dateFormat1.format(tanggal);

		SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
		String dateString = format.format(tanggal);

		List<TransaksiTelkom> data = transaksiTelkomRepository.laporanPenunggakanMonthly(bulan, tahun);
		String title = "Laporan Penunggakan bulan  " + dateString;

		helper.setTo("haha1hihi2huhu3@gmail.com");
		// helper.setCc("haha1hihi2huhu3@gmail.com");
		helper.setSubject("Laporan Penunggakan bulan " + dateString);
		helper.setText("Laporan penunggakan " + dateString, true);

		ByteArrayOutputStream pdf = transaksiTelkomService.ExportToPdfParam(data, title);
		helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

	// Excel
	// setiap tanggal 1 jam 7
	// @Scheduled(cron = "0 0 7 1 * *")
	//@Scheduled(cron = "${cron.monthly}")
	public void sendEmailDayExcel() throws Exception {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date tanggal = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		String bulan = dateFormat.format(tanggal);
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");
		String tahun = dateFormat1.format(tanggal);

		SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
		String dateString = format.format(tanggal);

		List<TransaksiTelkom> data = transaksiTelkomRepository.laporanPenunggakanMonthly(bulan, tahun);
		String title = "Laporan Penunggakan bulan  " + dateString;
		// List<TransaksiTelkomWrapper> listUsers =
		// transaksiTelkomService.findAllStatus1();
		helper.setTo("haha1hihi2huhu3@gmail.com");
		// helper.setCc("haha1hihi2huhu3@gmail.com");
		helper.setSubject("Laporan Penunggakan bulan " + dateString);
		helper.setText("Laporan penunggakan " + dateString, true);
		System.out.println("waiting...");
		// ByteArrayOutputStream pdf = transaksiTelkomService.ExportToPdfParam(data,
		// title);

		InputStreamSource laporan = transaksiTelkomService.ExportToExcelParam(data);
//			LaporanPenunggakanExcelExporter excelExporter = new LaporanPenunggakanExcelExporter(data);
//			ByteArrayOutputStream excel = excelExporter.export();

		helper.addAttachment(title + ".xlsx", laporan);
		javaMailSender.send(mailMessage);
		System.out.println("Email send");
	}

	// @Scheduled(cron = "* * * * * ?")
	public void tester() {
		List<String> a = laporanPenunggakanConfigurationProperties.getColumn();
		for (String b : a) {
			System.out.println(b);
		}

	}
}

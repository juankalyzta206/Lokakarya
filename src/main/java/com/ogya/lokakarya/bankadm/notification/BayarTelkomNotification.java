package com.ogya.lokakarya.bankadm.notification;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.service.HistoryBankService;

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
	@Scheduled(cron = "0 0 7 * * *")
	public void sendEmailDay() throws Exception{
			MimeMessage mailMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, - 1);
			Date tanggal = cal.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String yesterday = dateFormat.format(tanggal);
			
			SimpleDateFormat format = new SimpleDateFormat("EEEE dd/MM/yyyy", new Locale("in", "ID"));
			String dateString = format.format(tanggal);
			
			List<HistoryBank> data = historyBankRepository.laporanBayarTeleponToday(yesterday);
			String title = "Laporan Transaksi Bank Bayar Telepon Hari " + dateString;
						
			helper.setTo("usernamemeeting@gmail.com");
			helper.setCc("taerakim.21@gmail.com");
			helper.setSubject("Laporan Bayar Telepon Hari " + dateString);
			helper.setText("Laporan Bayar Hari "+ dateString, true);
			
			ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(data, title);
			helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
			javaMailSender.send(mailMessage);
			System.out.println("Email send");
	}
	
	@Scheduled(cron = "0 0 7 * * MON")
	public void sendEmailWeek() throws Exception{
			MimeMessage mailMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			
			Calendar calStart = Calendar.getInstance();
			calStart.add(Calendar.DATE, - 7);
			Date dateStart = calStart.getTime();
			
			Calendar calEnd = Calendar.getInstance();
			calEnd.add(Calendar.DATE, - 1);
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
	
	@Scheduled(cron = "0 0 7 1 * *")
	public void sendEmailMonth() throws Exception{
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
			helper.setSubject("Laporan Bayar Telepon Bulan " + bulan );
			helper.setText("Laporan Bayar Telepon Bulan "+ bulan, true);
			
			ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(data, title);
			helper.addAttachment(title + ".pdf", new ByteArrayResource(pdf.toByteArray()));
			javaMailSender.send(mailMessage);
			System.out.println("Email send");
	}
}

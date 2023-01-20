package com.ogya.lokakarya.bankadm.notification;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.ogya.lokakarya.bankadm.service.HistoryBankService;

@Service
@Transactional
public class BayarTelkomNotification {
	@Autowired
	HistoryBankService historyBankService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;

	
//	===========================================KirimEmail=====================================================
	@Scheduled(cron = "0 49 14 * * *")
	public void sendEmail() throws Exception{
			MimeMessage mailMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			Date date = new Date();
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, - 1);
			Date date2 = cal.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String yesterday = dateFormat.format(date2);
			System.out.println(yesterday);
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String dateString = format.format(date);
			
			
			helper.setTo("usernamemeeting@gmail.com");
			helper.setCc("dararyanti21@gmail.com");
			helper.setSubject("Laporan Bayar Telepon Tanggal: " + dateString);
			helper.setText("Laporan Bayar Telepon "+ dateString, true);
			
			ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(yesterday);
			helper.addAttachment("Laporan Bayar Telepon.pdf", new ByteArrayResource(pdf.toByteArray()));
			javaMailSender.send(mailMessage);
			System.out.println("Email send");
	}
	
	@Scheduled(cron = "20 41 14 * * *")
	public void sendEmailWeek() throws Exception{
			MimeMessage mailMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			Date date = new Date();
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, - 1);
			Date date2 = cal.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String yesterday = dateFormat.format(date2);
			System.out.println(yesterday);
			

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String start = format.format(date);	
			
			
			helper.setTo("usernamemeeting@gmail.com");
			helper.setCc("dararyanti21@gmail.com");
			helper.setSubject("Laporan Bayar Telepon Perminggu: " + start + "-" );
			helper.setText("Laporan Bayar Telepon "+ start, true);
			
			ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(yesterday);
			helper.addAttachment("Laporan Bayar Telepon.pdf", new ByteArrayResource(pdf.toByteArray()));
			javaMailSender.send(mailMessage);
			System.out.println("Email send");
	}
}

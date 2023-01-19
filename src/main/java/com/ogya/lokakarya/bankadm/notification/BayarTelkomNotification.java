package com.ogya.lokakarya.bankadm.notification;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.ogya.lokakarya.bankadm.service.HistoryBankService;

@Service
public class BayarTelkomNotification {
	@Autowired
	HistoryBankService historyBankService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;

	
//	===========================================KirimEmail=====================================================
	@Scheduled(cron = "0 33 13 * * ?")
	public void sendEmail(HttpServletResponse response) throws Exception{
		try {
			MimeMessage mailMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			LocalDate today = LocalDate.now();
			Date date = new Date();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dateString = dateFormat.format(date);
			
			
			helper.setTo("taerakim.21@gmail.com");
			helper.setSubject("Laporan Bayar Telepon " + dateString);
			helper.setText("Laporan Bayar Telepon "+ dateString, true);
			
			ByteArrayOutputStream pdf = historyBankService.ExportToPdfBayarTeleponParam(response, today);
			helper.addAttachment("Laporan Bayar Telepon.pdf", new ByteArrayResource(pdf.toByteArray()));
			javaMailSender.send(mailMessage);
			System.out.println("Email send");

		} catch (MessagingException e) {
			System.err.println("Failed send email");
			e.printStackTrace();
		}
	}
}

package com.ogya.lokakarya.exercise.feign.controller.nasabah;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.ogya.lokakarya.notification.bankadm.SetorNotification;

@RestController
@RequestMapping(value = "/laporanHistorySetor")
@CrossOrigin(origins = "*")
public class LaporanHistorySetorHarianController {
	@Autowired
	SetorNotification setor;
	
	@RequestMapping(value = "/laporanSetorDaily", method = RequestMethod.GET)
    public void notifSetorDaily() throws MessagingException, IOException, DocumentException {
		setor.historyNotificationDaily();
	}
	
	@RequestMapping(value = "/laporanSetorWeekly", method = RequestMethod.GET)
    public void notifSetorWeekly() throws MessagingException, IOException, DocumentException {
		setor.historyNotificationWeekly();
	}
	
	@RequestMapping(value = "/laporanSetorMonthly", method = RequestMethod.GET)
    public void notifSetorMonthly() throws MessagingException, IOException, DocumentException {
		setor.historyNotificationMonthly();
	}
}

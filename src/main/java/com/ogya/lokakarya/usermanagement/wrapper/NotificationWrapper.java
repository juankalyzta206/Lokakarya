package com.ogya.lokakarya.usermanagement.wrapper;


public class NotificationWrapper {
	private String subject;
	private String topHeader;
	private String botHeader;
	private String titlePdf;
	private String fileName;
	private String[] receiver;
	private String[] cc;
	
	
	public String[]getReceiver() {
		return receiver;
	}
	public void setReceiver(String[]receiver) {
		this.receiver = receiver;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTopHeader() {
		return topHeader;
	}
	public void setTopHeader(String topHeader) {
		this.topHeader = topHeader;
	}
	public String getBotHeader() {
		return botHeader;
	}
	public void setBotHeader(String botHeader) {
		this.botHeader = botHeader;
	}
	public String getTitlePdf() {
		return titlePdf;
	}
	public void setTitlePdf(String titlePdf) {
		this.titlePdf = titlePdf;
	}
	
	
}

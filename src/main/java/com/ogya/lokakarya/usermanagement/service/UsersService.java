package com.ogya.lokakarya.usermanagement.service;


import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.HakAksesRepository;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;
import com.ogya.lokakarya.usermanagement.repository.criteria.UsersCriteriaRepository;
import com.ogya.lokakarya.usermanagement.wrapper.NotificationWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersAddWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersRegisterWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersUpdateWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.UsersWrapper;
import com.ogya.lokakarya.usermanagement.wrapper.login.UsersLoginWrapper;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;

@Service
@Transactional
public class UsersService {
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	HakAksesRepository hakAksesRepository;
	
	@Autowired
	UsersCriteriaRepository usersCriteriaRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private String[] receiver = {"maulanairzan5@gmail.com"};
//	private String[] cc = {"taerakim.21@gmail.com", "eonjejjeumilkka@gmail.com"};
	private String[] cc = {};
	
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
		Locale id = new Locale("in", "ID");
		SimpleDateFormat f = new SimpleDateFormat("EEEE",id);
		String dayName = f.format(date);
		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Penambahan User");
		description.setTopHeader("Laporan Penambahan User Harian");
		description.setBotHeader("Hari : "+dayName+", "+day+"/"+month+"/"+year);
		description.setTitlePdf("Laporan Penambahan User Harian("+dayName+", "+day+" "+getMonthForInt(month)+" "+year+")");
		description.setFileName("LaporanPenambahanUserHarian("+day+"/"+month+"/"+year+")");
		List<Users> dailyData = usersRepository.newUsersDaily();
		ExportToPdfNotification(dailyData, description);
	}
	
	@Scheduled(cron = "0 0 7 1 * *") // <-- second, minute, hour, day, month
	public void MonthlyNotification() throws Exception {
		Date date = new Date();
		date = FindPrevDay(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Penambahan User");
		description.setTopHeader("Laporan Penambahan User Bulanan");
		description.setBotHeader("Bulan "+getMonthForInt(month)+" tahun "+year);
		description.setTitlePdf("Laporan Penambahan User Bulanan("+getMonthForInt(month)+" "+year+")");
		description.setFileName("LaporanPenambahanUserBulanan("+month+"/"+year+")");
		List<Users> monthlyData = usersRepository.newUsersMonthly();
		ExportToPdfNotification(monthlyData, description);
	}
	
	@Scheduled(cron = "0 0 7 * * SUN") // <-- second, minute, hour, day, month
	public void WeeklyNotification() throws Exception {
		Date date = new Date();
		date = FindPrevDay(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		Calendar calendarStartWeek = new GregorianCalendar();
		calendarStartWeek.setTime(date);
		calendarStartWeek.add(Calendar.DATE, -6);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int yearStartWeek = calendarStartWeek.get(Calendar.YEAR);
		int monthStartWeek = calendarStartWeek.get(Calendar.MONTH) + 1;
		int dayStartWeek = calendarStartWeek.get(Calendar.DAY_OF_MONTH);
		Locale id = new Locale("in", "ID");
		SimpleDateFormat f = new SimpleDateFormat("EEEE",id);
		String dayName = f.format(date);
		String dayNameStartWeek = f.format(FindStartWeek(date));
		NotificationWrapper description = new NotificationWrapper();
		description.setReceiver(receiver);
		description.setCc(cc);
		description.setSubject("Laporan Penambahan User");
		description.setTopHeader("Laporan Penambahan User Mingguan");
		description.setBotHeader("Hari : "+dayNameStartWeek+", "+dayStartWeek+"/"+monthStartWeek+"/"+yearStartWeek+"-"+dayName+", "+day+"/"+month+"/"+year);
		description.setTitlePdf("Laporan Penambahan User Mingguan("+dayNameStartWeek+", "+dayStartWeek+"/"+monthStartWeek+"/"+yearStartWeek+"-"+dayName+", "+day+"/"+month+"/"+year+")");
		description.setFileName("LaporanPenambahanUserMingguan("+dayNameStartWeek+", "+dayStartWeek+"/"+monthStartWeek+"/"+yearStartWeek+"-"+dayName+", "+day+"/"+month+"/"+year+")");
		List<Users> weeklyData = usersRepository.newUsersWeekly();
		ExportToPdfNotification(weeklyData, description);
	}
	
	
	String getMonthForInt(int num) {
		num = num-1;
		String month = "wrong";
		Locale id = new Locale("in", "ID");
    	DateFormatSymbols dfs = new DateFormatSymbols(id);
    	String[] months = dfs.getMonths();
    	if (num >= 0 && num <= 12) {
    		month = months[num];
    	}
    	return month;
	}
	
	private static Date FindPrevDay(Date date)
	{
	  return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}
	
	private static Date FindStartWeek(Date date)
	{
	  return new Date(date.getTime() - 6*MILLIS_IN_A_DAY);
	}
	
	
	public PaginationList<UsersWrapper, Users> ListWithPaging(PagingRequestWrapper request) { 
		List<Users> usersList = usersCriteriaRepository.findByFilter(request);
		int fromIndex = (request.getPage())* (request.getSize());
		int toIndex = Math.min(fromIndex + request.getSize(), usersList.size());
		Page<Users> usersPage = new PageImpl<>(usersList.subList(fromIndex, toIndex), PageRequest.of(request.getPage(), request.getSize()), usersList.size());
		List<UsersWrapper> usersWrapperList = new ArrayList<>();
		for(Users entity : usersPage) {
		    usersWrapperList.add(toWrapper(entity));
		}
		return new PaginationList<UsersWrapper, Users>(usersWrapperList, usersPage);	
	}
	
	private String hashPassword(String plainPassword) {
        return new BCryptPasswordEncoder().encode(plainPassword);
    }
	
	private Boolean matchPassword(String plainPassword, String databasePassword) {
		return new BCryptPasswordEncoder().matches(plainPassword, databasePassword);
	}
	

	public List<UsersLoginWrapper> findByEmailOrUsernameAndPassword(String identity, String password) {
		if (usersRepository.isRegisteredEmail(identity) == 0) {
			if (usersRepository.isRegisteredUsername(identity) == 0) {
				throw new BusinessException("Email or Username is not Registered");
			} else {
				String databasePassword = usersRepository.hashedPasswordUsername(identity);
				if (!matchPassword(password, databasePassword)) {
					throw new BusinessException("Wrong Password");
				} else {
					List<Users> loginList = usersRepository.findByUsernameAndPassword(identity, databasePassword);
					return toWrapperListLogin(loginList);
				}
			}
		} else {
			String databasePassword = usersRepository.hashedPasswordEmail(identity);
			if (!matchPassword(password, databasePassword)) {
				throw new BusinessException("Wrong Password");
			} else {
				List<Users> loginList = usersRepository.findByEmailAndPassword(identity, databasePassword);
				return toWrapperListLogin(loginList);
			}
		}
	}
	
	public PaginationList<UsersWrapper, Users> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size, Sort.by("userId").ascending());
		Page<Users> usersPage = usersRepository.findAll(paging);
		List<Users> usersList = usersPage.getContent();
		List<UsersWrapper> usersWrapperList = toWrapperList(usersList);
		return new PaginationList<UsersWrapper, Users>(usersWrapperList, usersPage);
	}
	
	
	public List<UsersWrapper> findByEmailAndPassword(String email, String password) {
		List<Users> loginList = usersRepository.findByEmailAndPassword(email, password);
		return toWrapperList(loginList);
	}
	

	private UsersWrapper toWrapper(Users entity) {
		UsersWrapper wrapper = new UsersWrapper();
		wrapper.setUserId(entity.getUserId());
		wrapper.setUsername(entity.getUsername());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setEmail(entity.getEmail());
		wrapper.setTelp(entity.getTelp());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}
	
	private UsersRegisterWrapper toWrapperRegister(Users entity) {
		UsersRegisterWrapper wrapper = new UsersRegisterWrapper();
		wrapper.setUserId(entity.getUserId());
		wrapper.setUsername(entity.getUsername());
		wrapper.setPassword(entity.getPassword());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setEmail(entity.getEmail());
		wrapper.setTelp(entity.getTelp());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}
	
	private UsersAddWrapper toWrapperAdd(Users entity) {
		UsersAddWrapper wrapper = new UsersAddWrapper();
		wrapper.setUserId(entity.getUserId());
		wrapper.setUsername(entity.getUsername());
		wrapper.setPassword(entity.getPassword());
		wrapper.setNama(entity.getNama());
		wrapper.setAlamat(entity.getAlamat());
		wrapper.setEmail(entity.getEmail());
		wrapper.setTelp(entity.getTelp());
		wrapper.setProgramName(entity.getProgramName());
		wrapper.setCreatedDate(entity.getCreatedDate());
		wrapper.setCreatedBy(entity.getCreatedBy());
		wrapper.setUpdatedDate(entity.getUpdatedDate());
		wrapper.setUpdatedBy(entity.getUpdatedBy());
		return wrapper;
	}
	
	
	private UsersLoginWrapper toWrapperLogin(Users entity) {
		UsersLoginWrapper wrapper = new UsersLoginWrapper();
		wrapper.setUsername(entity.getUsername());
		wrapper.setHakAkses(entity.getHakAkses());		
		wrapper.setNama(entity.getNama());
		return wrapper;
	}
	

	private List<UsersWrapper> toWrapperList(List<Users> entityList) {
		List<UsersWrapper> wrapperList = new ArrayList<UsersWrapper>();
		for (Users entity : entityList) {
			UsersWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	private List<UsersLoginWrapper> toWrapperListLogin(List<Users> entityList) {
		List<UsersLoginWrapper> wrapperList = new ArrayList<UsersLoginWrapper>();
		for (Users entity : entityList) {
			UsersLoginWrapper wrapper = toWrapperLogin(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}
	
	public List<UsersWrapper> findAll() {
		List<Users> userList = usersRepository.findAll(Sort.by(Order.by("userId")).ascending());
		return toWrapperList(userList);
	}
	
	public List<Users> findListUser(){
		List<Users> userList = usersRepository.findAll(Sort.by(Order.by("userId")).ascending());
		return userList;
	}
	
	
	public List<UsersWrapper> findByUserId(Long userId) {
		List<Users> userList = usersRepository.findByUserId(userId);
		return toWrapperList(userList);
	}
	
	private Users toEntityUpdate(UsersUpdateWrapper wrapper) {
		Users entity = new Users();
		if (wrapper.getUserId() != null) {
			entity = usersRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setUsername(wrapper.getUsername());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setEmail(wrapper.getEmail());
		entity.setTelp(wrapper.getTelp());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}
	
	private Users toEntityRegister(UsersRegisterWrapper wrapper) {
		Users entity = new Users();
		if (wrapper.getUserId() != null) {
			entity = usersRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setUsername(wrapper.getUsername());
		entity.setPassword(wrapper.getPassword());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setEmail(wrapper.getEmail());
		entity.setTelp(wrapper.getTelp());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}
	
	private Users toEntityAdd(UsersAddWrapper wrapper) {
		Users entity = new Users();
		if (wrapper.getUserId() != null) {
			entity = usersRepository.getReferenceById(wrapper.getUserId());
		}
		entity.setUsername(wrapper.getUsername());
		entity.setPassword(wrapper.getPassword());
		entity.setNama(wrapper.getNama());
		entity.setAlamat(wrapper.getAlamat());
		entity.setEmail(wrapper.getEmail());
		entity.setTelp(wrapper.getTelp());
		entity.setProgramName(wrapper.getProgramName());
		entity.setCreatedDate(wrapper.getCreatedDate());
		entity.setCreatedBy(wrapper.getCreatedBy());
		entity.setUpdatedDate(wrapper.getUpdatedDate());
		entity.setUpdatedBy(wrapper.getUpdatedBy());
		return entity;
	}

	public UsersRegisterWrapper register(UsersRegisterWrapper wrapper) {
		if (usersRepository.checkUsername(wrapper.getUsername())==0) {
			if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
				wrapper.setPassword(hashPassword(wrapper.getPassword()));
				Users user = usersRepository.save(toEntityRegister(wrapper));
				return toWrapperRegister(user);
			} else {
				throw new BusinessException("Email already taken");
			}
		} else {
			throw new BusinessException("Username already taken");
		}
	}
	
	
	public UsersAddWrapper save(UsersAddWrapper wrapper) {
		if (usersRepository.checkUsername(wrapper.getUsername())==0) {
			if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
				wrapper.setPassword(hashPassword(wrapper.getPassword()));
				Users user = usersRepository.save(toEntityAdd(wrapper));
				return toWrapperAdd(user);
			} else {
				throw new BusinessException("Email already taken");
			}
		} else {
			throw new BusinessException("Username already taken");
		}
	}
	
	public UsersWrapper update(UsersUpdateWrapper wrapper) {
		if (wrapper.getSameUsername() == 0) {
			if (wrapper.getSameEmail() == 0) {
				if (usersRepository.checkUsername(wrapper.getUsername())==0) {
					if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
						Users user = usersRepository.save(toEntityUpdate(wrapper));
						return toWrapper(user);
					} else {
						throw new BusinessException("Email already taken");
					}
				} else if (usersRepository.checkUsername(wrapper.getUsername())==1){
					if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
						throw new BusinessException("Username already taken");
					}
				}
				else {
					throw new BusinessException("Username and email already taken");
				}
			}
			if (wrapper.getSameEmail() == 1) {
				if (usersRepository.checkUsername(wrapper.getUsername()) == 0) {
					Users user = usersRepository.save(toEntityUpdate(wrapper));
					return toWrapper(user);
				} else {
					throw new BusinessException("Username already taken");
				}
			}
		} else {
			if (wrapper.getSameEmail() == 0) {
				if (usersRepository.checkEmail(wrapper.getEmail()) == 0) {
					Users user = usersRepository.save(toEntityUpdate(wrapper));
					return toWrapper(user);
				} else {
					throw new BusinessException("Email already taken");
				}
			}
			if (wrapper.getSameEmail() == 1) {
				Users user = usersRepository.save(toEntityUpdate(wrapper));
				return toWrapper(user);
			}
		}
		throw new BusinessException("Wrong input");
	}

	public void delete(Long id) {
		if (usersRepository.isExistHakAkses(id) == 0) {
			usersRepository.deleteById(id);
		} else {
			throw new BusinessException("User ID cannot deleted. User ID is still used in the HAK_AKSES table");
		}
		
	}
	
	public void SendEmailWithAttachment(InputStreamSource data,NotificationWrapper description) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setTo(description.getReceiver());
			mimeMessageHelper.setCc(description.getCc());
			mimeMessageHelper.setSubject(description.getSubject());
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("<h3>"+description.getTopHeader()+"</h3>");
			buffer.append("<h5>"+description.getBotHeader()+"</h5>");
			String body = buffer.toString();
			mimeMessageHelper.setText(body, true);
			
			mimeMessageHelper.addAttachment(description.getFileName()+".pdf", data);
			javaMailSender.send(mimeMessage);
			System.out.println("Email sent");
		} catch (MessagingException e){
			System.err.print("Failed send email");
			e.printStackTrace();
		}
	}
	
	public void ExportToPdfNotification(List<Users> data, NotificationWrapper description) throws Exception{
		 // Now create a new iText PDF document
	    Document pdfDoc = new Document(PageSize.A4.rotate());
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, baos);
	    pdfDoc.open();
	    
	    Paragraph title = new Paragraph(description.getTitlePdf(),
	            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);
	    
	    // Add the generation date
	    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

	    // Create a table
	    PdfPTable pdfTable = new PdfPTable(10); 
	    

	    pdfTable.setWidthPercentage(100);
	    pdfTable.setSpacingBefore(10f);
	    pdfTable.setSpacingAfter(10f);
	         
	  
	        pdfTable.addCell("Username");
	        pdfTable.addCell("Nama");
	        pdfTable.addCell("Alamat");
	        pdfTable.addCell("Email");
	        pdfTable.addCell("Telp");
	        pdfTable.addCell("Program Name");
	        pdfTable.addCell("Created Date");
	        pdfTable.addCell("Created By");
	        pdfTable.addCell("Updated Date");
	        pdfTable.addCell("Updated By");  
	        BaseColor color = new BaseColor(135,206,235);
	    	for(int i=0;i<10;i++) {
	    		pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
	    	}
	    
	    // Iterate through the data and add it to the table
	    for (Users entity : data) {
	    	pdfTable.addCell(String.valueOf(entity.getUsername() != null ? String.valueOf(entity.getUsername()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getEmail() != null ? String.valueOf(entity.getEmail()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getTelp() != null ? String.valueOf(entity.getTelp()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getProgramName() != null ? String.valueOf(entity.getProgramName()) : "-"));
	    	
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    	String createdDate = "-";
	    	if (entity.getCreatedDate() != null) {
	    		createdDate = formatter.format(entity.getCreatedDate());
	    	}
	    	pdfTable.addCell(createdDate);
	    	pdfTable.addCell(String.valueOf(entity.getCreatedBy() != null ? String.valueOf(entity.getCreatedBy()) : "-"));
	    	
	    	String updatedDate = "-";
	    	if (entity.getUpdatedDate() != null) {
	    		updatedDate = formatter.format(entity.getUpdatedDate());
		    	}
	    	pdfTable.addCell(updatedDate);
	    	pdfTable.addCell(String.valueOf(entity.getUpdatedBy() != null ? String.valueOf(entity.getUpdatedBy()) : "-"));
	    	
	    }
	    
	    // Add the table to the pdf document
	    pdfDoc.add(pdfTable);

	    pdfDoc.close();
	    pdfWriter.close();
	    byte[] bytes = baos.toByteArray();
	    InputStreamSource attachmentSource = new ByteArrayResource(bytes);
	    SendEmailWithAttachment(attachmentSource, description);
	    
	}
	
	
	public void ExportToPdf(HttpServletResponse response) throws Exception{
		 // Call the findAll method to retrieve the data
	    List<Users> data = usersRepository.findAll();
		
		
	    // Now create a new iText PDF document
	    Document pdfDoc = new Document(PageSize.A4.rotate());
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
	    pdfDoc.open();
	    
	    Paragraph title = new Paragraph("List Users",
	            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);
	    
	    // Add the generation date
	    pdfDoc.add(new Paragraph("Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

	    // Create a table
	    PdfPTable pdfTable = new PdfPTable(10); 
	    

	    pdfTable.setWidthPercentage(100);
	    pdfTable.setSpacingBefore(10f);
	    pdfTable.setSpacingAfter(10f);
	         
	  
	        pdfTable.addCell("Username");
	        pdfTable.addCell("Nama");
	        pdfTable.addCell("Alamat");
	        pdfTable.addCell("Email");
	        pdfTable.addCell("Telp");
	        pdfTable.addCell("Program Name");
	        pdfTable.addCell("Created Date");
	        pdfTable.addCell("Created By");
	        pdfTable.addCell("Updated Date");
	        pdfTable.addCell("Updated By");  
	        BaseColor color = new BaseColor(135,206,235);
	    	for(int i=0;i<10;i++) {
	    		pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
	    	}
	    
	    // Iterate through the data and add it to the table
	    for (Users entity : data) {
	    	pdfTable.addCell(String.valueOf(entity.getUsername() != null ? String.valueOf(entity.getUsername()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getAlamat() != null ? String.valueOf(entity.getAlamat()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getEmail() != null ? String.valueOf(entity.getEmail()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getTelp() != null ? String.valueOf(entity.getTelp()) : "-"));
	    	pdfTable.addCell(String.valueOf(entity.getProgramName() != null ? String.valueOf(entity.getProgramName()) : "-"));
	    	
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    	String createdDate = "-";
	    	if (entity.getCreatedDate() != null) {
	    		createdDate = formatter.format(entity.getCreatedDate());
	    	}
	    	pdfTable.addCell(createdDate);
	    	pdfTable.addCell(String.valueOf(entity.getCreatedBy() != null ? String.valueOf(entity.getCreatedBy()) : "-"));
	    	
	    	String updatedDate = "-";
	    	if (entity.getUpdatedDate() != null) {
	    		updatedDate = formatter.format(entity.getUpdatedDate());
		    	}
	    	pdfTable.addCell(updatedDate);
	    	pdfTable.addCell(String.valueOf(entity.getUpdatedBy() != null ? String.valueOf(entity.getUpdatedBy()) : "-"));
	    	
	    }
	    
	    // Add the table to the pdf document
	    pdfDoc.add(pdfTable);

	    pdfDoc.close();
	    pdfWriter.close();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}
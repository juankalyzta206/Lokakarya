package com.ogya.lokakarya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.ogya.lokakarya.usermanagement.feign.request.UsersFeignRequest;
import com.ogya.lokakarya.usermanagement.feign.response.UsersFeignResponse;
import com.ogya.lokakarya.usermanagement.feign.services.UsersFeignServices;
import com.ogya.lokakarya.usermanagement.service.RolesService;
import com.ogya.lokakarya.usermanagement.service.UsersService;

@EnableFeignClients
@SpringBootApplication
public class LokakaryaApplication implements CommandLineRunner{
	@Autowired 
	UsersFeignServices usersFeignServices;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	RolesService rolesService;

	public static void main(String[] args) {
		SpringApplication.run(LokakaryaApplication.class, args);
	}
	
	@Override
	public void run(String...args) throws Exception{
		String noTelepon = "0857";
		String alamat = "Mangkudranan, Margorejo, Tempel";
		String nama = "Irzan Maulana";
		String email = "maulanairzan5@gmail.com";
		String password = "maulanairzan5";
		String username = "maulanairzan5";
		
		String role = "useradm";

		
		UsersFeignRequest request = new UsersFeignRequest();
		request.setAlamat(alamat);
		request.setEmail(email);
		request.setNama(nama);
		request.setTelpon(noTelepon);
		
		System.out.println("");
		System.out.println("Call user role inquiry");
		UsersFeignResponse userRoleInquiry = usersFeignServices.callUserRoleInquiry(role);
		System.out.println("Program Name : "+userRoleInquiry.getProgramName());
		System.out.println("Success : "+userRoleInquiry.getSuccess());
		if (userRoleInquiry.getSuccess()) {
			try {
				rolesService.saveRolesFromWebService(userRoleInquiry, role);
			} catch (Exception e){
				System.err.print("Failed to create Role");
				e.printStackTrace();
			}
		}
		
		
		
		System.out.println("");
		System.out.println("Call user role record");
		UsersFeignResponse usersFeignResponse = usersFeignServices.callUserRoleRecord(request);
		System.out.println("Program Name : "+usersFeignResponse.getProgramName());
		System.out.println("Success : "+usersFeignResponse.getSuccess());
		if (usersFeignResponse.getSuccess()) {
			try {
				usersService.saveUsersFromWebService(username, password, usersFeignResponse, request);
			} catch (Exception e){
				System.err.print("Failed to create User");
				e.printStackTrace();
			}
			
		}
		
		
		
		
	}

}

package com.ogya.lokakarya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.ogya.lokakarya.usermanagement.feign.request.UsersFeignRequest;
import com.ogya.lokakarya.usermanagement.feign.response.UsersFeignResponse;
import com.ogya.lokakarya.usermanagement.feign.services.UsersFeignServices;

@EnableFeignClients
@SpringBootApplication
public class LokakaryaApplication implements CommandLineRunner{
	@Autowired 
	UsersFeignServices usersFeignServices;

	public static void main(String[] args) {
		SpringApplication.run(LokakaryaApplication.class, args);
	}
	
	@Override
	public void run(String...args) throws Exception{
		String noTelepon = "0857";
		String noRekening = "1234";
		String nama = "Irzan Maulana";
		String email = "maulanairzan5@gmail.com";
		
		UsersFeignRequest request = new UsersFeignRequest();
		request.setAlamat(noTelepon);
		request.setEmail(email);
		request.setNama(nama);
		request.setTelpon(noRekening);
		
		System.out.println("");
		System.out.println("Call user role record");
		UsersFeignResponse usersFeignResponse = usersFeignServices.callUserRoleRecord(request);
		System.out.println("Program Name : "+usersFeignResponse.getProgramName());
		System.out.println("Success : "+usersFeignResponse.getSuccess());
		
		System.out.println("LLLLLLLLLLLLLLLLL");
	}

}

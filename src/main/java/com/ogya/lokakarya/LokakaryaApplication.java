package com.ogya.lokakarya;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

@SpringBootApplication
public class LokakaryaApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(LokakaryaApplication.class, args);
	}
	
	@Override
	public void run(String...args) throws Exception{
		System.out.println("LLLLLLLLLLLLLLLLL");
	}

}

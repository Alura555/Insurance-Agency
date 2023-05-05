package com.example.insuranceagency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class InsuranceAgencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceAgencyApplication.class, args);
	}

}

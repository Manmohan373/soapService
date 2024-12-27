package com.alfaris.ipsh.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("com.alfaris.ipsh.soap.repository")
//@EntityScan("com.alfaris.ipsh.soap.entity") 
public class SoapService1Application {

	public static void main(String[] args) {
		SpringApplication.run(SoapService1Application.class, args);
	}

}

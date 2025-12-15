package com.vikas.EZmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EZmanageApplication {

	public static void main(String[] args) {
		SpringApplication.run(EZmanageApplication.class, args);
	}

}

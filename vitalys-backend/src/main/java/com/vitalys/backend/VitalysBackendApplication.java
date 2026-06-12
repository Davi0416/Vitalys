package com.vitalys.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VitalysBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitalysBackendApplication.class, args);
	}

}

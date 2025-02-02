package com.example.health_management;

import com.example.health_management.application.validate.XSSProtection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthManagementApplication.class, args);
		String s = XSSProtection.sanitizeInput("test");
	}
}

package com.example.health_management;

import com.example.health_management.domain.entities.Payload;
import com.example.health_management.application.guards.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class HealthManagementApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Context loaded successfully");
	}

}

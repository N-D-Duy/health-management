package com.example.health_management;

import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class HealthManagementApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Context loaded successfully");
	}

	@Test
	void generateKeyPair(){
		JwtService jwtServiceTest = new JwtService();
		Map<String, String> keyPair = jwtServiceTest.generateKeyPair();

		System.out.println("Public key: " + keyPair.get("publicKey"));
		System.out.println("Private key: " + keyPair.get("privateKey"));

		Payload payload = new Payload(1, "ROLE_ADMIN", "duy999@gmail.com");
		String token = jwtServiceTest.generateToken(payload, keyPair.get("privateKey"), 900000);
		System.out.println("Token: " + token);
	}
}

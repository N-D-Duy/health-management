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

	@Test
	void generateKeyPair(){
		JwtProvider jwtProviderTest = new JwtProvider();
		Map<String, String> keyPair = jwtProviderTest.generateKeyPair();

		System.out.println("Public key: " + keyPair.get("publicKey"));
		System.out.println("Private key: " + keyPair.get("privateKey"));

		Payload payload = new Payload(1, "ROLE_ADMIN", "duy999@gmail.com");
		String token = jwtProviderTest.generateToken(payload, keyPair.get("privateKey"), 900000);
		System.out.println("Token: " + token);
	}
}

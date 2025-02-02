package com.example.health_management;

import com.example.health_management.application.guards.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class HealthManagementApplicationTests {


	@Test
	void contextLoads() {
		System.out.println("Context loaded successfully");
	}

	@Test
	void genKeyPair(){
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);
			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();

			// convert keys to PEM string for storage
			String privateKeyPEM = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());
			String publicKeyPEM = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());

			log.info(publicKeyPEM);
			log.info(privateKeyPEM);
		} catch(NoSuchAlgorithmException e){
			log.error("Error: " + e.getMessage());
		}
	}
}

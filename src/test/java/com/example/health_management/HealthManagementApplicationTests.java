package com.example.health_management;

import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderRequest;
import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderResponse;
import com.example.health_management.application.DTOs.payment.ZaloPayRefundRequest;
import com.example.health_management.application.DTOs.payment.ZaloPayRefundResponse;
import com.example.health_management.common.Constants;
import com.example.health_management.common.utils.zalopay.h_mac.ZaloPayHelper;
import com.example.health_management.domain.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.security.*;
import java.time.Duration;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class HealthManagementApplicationTests {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ObjectMapper objectMapper;


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

	@Test
	void testCallZaloPayApi(){
		try {
			MerchantAppCreateOrderRequest merchantAppCreateOrderRequest = new MerchantAppCreateOrderRequest();
			merchantAppCreateOrderRequest.setAmount(Integer.toUnsignedLong(100000));
			merchantAppCreateOrderRequest.setDescription("Test api");
			merchantAppCreateOrderRequest.setUserId(Integer.toUnsignedLong(1234));
			MerchantAppCreateOrderResponse merchantAppCreateOrderResponse = paymentService.createZaloPaymentOrder(merchantAppCreateOrderRequest);
			Assert.notNull(merchantAppCreateOrderResponse.getZpTransToken());
			System.out.println(merchantAppCreateOrderResponse.toString());
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testRefundApiUsingAppointmentId() {
		try {
			paymentService.refundAppointmentTransaction(28L);
		} catch (Exception e) {
			log.error("Error during refund: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testQueryRefundStatus() {
		try {
			paymentService.queryRefundStatus(28L);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

package com.example.health_management;

import com.example.health_management.application.DTOs.prescription.PrescriptionResponseDto;
import com.example.health_management.application.mapper.prescription.PrescriptionMapper;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.repositories.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class HealthManagementApplicationTests {


	@Test
	void contextLoads() {
		System.out.println("Context loaded successfully");
	}


}

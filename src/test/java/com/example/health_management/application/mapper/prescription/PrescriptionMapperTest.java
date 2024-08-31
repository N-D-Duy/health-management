//package com.example.health_management.application.mapper.prescription;
//
//import com.example.health_management.application.DTOs.prescription.CreatePrescriptionDto;
//import com.example.health_management.domain.entities.Medication;
//import com.example.health_management.domain.entities.Prescription;
//import com.example.health_management.domain.entities.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PrescriptionMapperTest {
//    private PrescriptionMapper prescriptionMapper;
//    private CreatePrescriptionDto createPrescriptionDto;
//    private Set<Medication> medications;
//
//    @BeforeEach
//    void setUp() {
//        medications = Set.of(Medication.builder().id(1).name("medication 01").build(), Medication.builder().id(2).name("medication 02").build());
//        createPrescriptionDto = CreatePrescriptionDto.builder().create_date(LocalDate.now()).diagnosis("Fever").treatment("treatment itiner 01").userId(7).medicationIds(Set.of(1, 2)).build();
//        prescriptionMapper = new PrescriptionMapperImpl();
//    }
//
//    @Test
//    void toPrescription() {
//        System.out.println("Testing toPrescription...");
//        Prescription prescription = prescriptionMapper.toPrescription(createPrescriptionDto, null, medications);
//        assertNotNull(prescription.getUser(),"null object" );
//    }
//
//    @Test
//    void toPrescriptionResponseDto() {
//    }
//
//    @Test
//    void partialUpdate() {
//    }
//}
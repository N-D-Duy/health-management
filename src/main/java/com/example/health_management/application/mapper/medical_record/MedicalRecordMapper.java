package com.example.health_management.application.mapper.medical_record;
import com.example.health_management.application.DTOs.medical_record.CreateMedicalRecordDto;
import com.example.health_management.application.DTOs.medical_record.MedicalRecordResponseDto;
import com.example.health_management.domain.entities.Appointment;
import com.example.health_management.domain.entities.MedicalRecord;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

     MedicalRecordResponseDto toMedicalRecordResponseDto(MedicalRecord medicalRecord);

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "prescription", source = "prescription")
     @Mapping(target = "user", source = "user")
     @Mapping(target = "appointment", source = "appointment")
     MedicalRecord toMedicalRecord(CreateMedicalRecordDto createMedicalRecordDto, Prescription prescription, User user, Appointment appointment);

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "prescription", source = "prescription")
     @Mapping(target = "user", source = "user")
     @Mapping(target = "appointment", source = "appointment")
     @Mapping(target = "note", source = "createMedicalRecordDto.note")
     @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
     MedicalRecord partialUpdate(@MappingTarget MedicalRecord medicalRecord, CreateMedicalRecordDto createMedicalRecordDto, User user, Prescription prescription, Appointment appointment);
}

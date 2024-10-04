package com.example.health_management.application.mapper.appointment_record;
import com.example.health_management.application.DTOs.medical_record.AppointmentRecordResponseDto;
import com.example.health_management.application.DTOs.medical_record.CreateAppointmentRecordDto;
import com.example.health_management.domain.entities.Appointment;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentRecordMapper {

    AppointmentRecordResponseDto toAppointmentRecordResponseDto(AppointmentRecord appointmentRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", source = "prescription")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "appointment", source = "appointment")
    @Mapping(target = "note", source = "createAppointmentRecordDto.note")
    AppointmentRecord toAppointmentRecord(CreateAppointmentRecordDto createAppointmentRecordDto, Prescription prescription, User user, Appointment appointment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", source = "prescription")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "appointment", source = "appointment")
    @Mapping(target = "note", source = "createAppointmentRecordDto.note")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    AppointmentRecord partialUpdate(@MappingTarget AppointmentRecord medicalRecord, CreateAppointmentRecordDto createAppointmentRecordDto, User user, Prescription prescription, Appointment appointment);

}

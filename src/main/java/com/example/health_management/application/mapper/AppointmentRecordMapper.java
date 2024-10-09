package com.example.health_management.application.mapper;
import com.example.health_management.application.DTOs.appointment_record.request.CreateAppointmentRecordRequest;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PrescriptionMapper.class, UserMapper.class})
public interface AppointmentRecordMapper {

    AppointmentRecordDTO toDTO(AppointmentRecord appointmentRecord);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", source = "prescription")
    @Mapping(target = "user", source = "user")
    AppointmentRecord toEntity(CreateAppointmentRecordRequest createAppointmentRecordRequest, Prescription prescription, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", source = "prescription")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    AppointmentRecord partialUpdate(@MappingTarget AppointmentRecord medicalRecord, CreateAppointmentRecordRequest createAppointmentRecordRequest, User user, Prescription prescription);

}

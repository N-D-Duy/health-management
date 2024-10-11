package com.example.health_management.application.mapper;
import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.domain.entities.AppointmentRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PrescriptionMapper.class, UserMapper.class, DoctorMapper.class})
public interface AppointmentRecordMapper {

    AppointmentRecordDTO toDTO(AppointmentRecord appointmentRecord);

    @Mapping(target = "id", ignore = true)
    AppointmentRecord toEntity(AppointmentRecordRequestDTO appointmentRecordRequestDTO);


    @Mapping(target = "id", ignore = true)
    AppointmentRecord toEntity(AppointmentRecordDTO appointmentRecordDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "healthProvider", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    AppointmentRecord update(@MappingTarget AppointmentRecord medicalRecord, UpdateAppointmentRequestDTO updateAppointmentRecord);
}

package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.user.request.UpdateDoctorRequest;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, AddressMapper.class, DoctorMapper.class})
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    User toEntity(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "doctorProfile", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "appointmentRecords", ignore = true)
    @Mapping(target = "allergies", ignore = true)
    @Mapping(target = "account", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    User update(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    UserSummaryDTO toUserSummaryDTO(User user);
}
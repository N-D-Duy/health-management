package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.user.request.UpdateDoctorRequest;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, AddressMapper.class})
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", source = "doctorProfile.id")
    @Mapping(target = "specialization", source = "doctorProfile.specialization")
    @Mapping(target = "experience", source = "doctorProfile.experience")
    @Mapping(target = "qualification", source = "doctorProfile.qualification")
    @Mapping(target = "rating", source = "doctorProfile.rating")
    @Mapping(target = "about", source = "doctorProfile.about")
    DoctorDTO toDoctorDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    User toEntity(UserDTO userDTO);


    Doctor toDoctorEntity(DoctorDTO doctorDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "doctorProfile", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(source = "updateUserRequest.firstName", target = "firstName")
    @Mapping(source = "updateUserRequest.lastName", target = "lastName")
    @Mapping(source = "updateUserRequest.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "updateUserRequest.gender", target = "gender")
    @Mapping(source = "updateUserRequest.avatarUrl", target = "avatarUrl")
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromDTO(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "updateDoctorRequest.specialization", target = "specialization")
    @Mapping(source = "updateDoctorRequest.experience", target = "experience")
    @Mapping(source = "updateDoctorRequest.qualification", target = "qualification")
    @Mapping(source = "updateDoctorRequest.rating", target = "rating")
    @Mapping(source = "updateDoctorRequest.about", target = "about")
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    Doctor updateDoctorFromDTO(UpdateDoctorRequest updateDoctorRequest, @MappingTarget Doctor doctor);
}

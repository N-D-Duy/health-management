package com.example.health_management.application.mapper.user;

import com.example.health_management.application.DTOs.user.UserResponseDto;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
//    User toEntity(UserResponseDto userDto);
}

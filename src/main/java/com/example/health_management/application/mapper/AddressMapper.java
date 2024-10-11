package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.address.AddressDTO;
import com.example.health_management.domain.entities.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDTO(Address address);

    Address toEntity(AddressDTO addressDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",ignore = true)
    Address updateAddress(AddressDTO addressDTO, @MappingTarget Address address);
}
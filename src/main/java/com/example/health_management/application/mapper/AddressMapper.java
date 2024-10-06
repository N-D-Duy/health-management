package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.address.request.AddressDTO;
import com.example.health_management.domain.entities.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address toEntityFromAddressRequestDto(AddressDTO addressDto, Long countryId);

    AddressDTO toAddressResponseDto(Address address);
    Address toEntityFromAddressResponseDto(AddressDTO addressDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",ignore = true)
    Address partialUpdateFromAddressRequestDto(AddressDTO addressDTO, @MappingTarget Address address);
}

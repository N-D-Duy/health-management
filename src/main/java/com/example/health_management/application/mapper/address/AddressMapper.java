package com.example.health_management.application.mapper.address;

import com.example.health_management.application.DTOs.address.AddressRequestDto;
import com.example.health_management.application.DTOs.address.AddressResponseDto;
import com.example.health_management.domain.entities.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address toEntityFromAddressRequestDto(AddressRequestDto addressDto, Long countryId);

    AddressResponseDto toAddressResponseDto(Address address);
    Address toEntityFromAddressResponseDto(AddressResponseDto addressDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",ignore = true)
    Address partialUpdateFromAddressRequestDto(AddressRequestDto addressRequestDto, @MappingTarget Address address);
}

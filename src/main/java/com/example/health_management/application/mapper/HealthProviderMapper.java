package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderDTO;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderSummary;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderWithDoctorsDTO;
import com.example.health_management.domain.entities.HealthProvider;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, AddressMapper.class})
public interface HealthProviderMapper {
    HealthProvider toEntity(HealthProviderDTO healthProviderDTO);


    HealthProviderDTO toDTO(HealthProvider healthProvider);
    HealthProviderSummary toSummary(HealthProvider healthProvider);
    HealthProviderWithDoctorsDTO toDTOWithDoctors(HealthProvider healthProvider);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    HealthProvider update(HealthProviderDTO healthProviderDTO, @MappingTarget HealthProvider healthProvider);

}

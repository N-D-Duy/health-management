package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.domain.entities.Logging;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoggingMapper {
    @Mapping(target = "id", ignore = true)
    Logging toEntity(LoggingDTO loggingDTO);

    LoggingDTO toDTO(Logging logging);
}

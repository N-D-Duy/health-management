package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.notification.NotificationDTO;
import com.example.health_management.domain.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "id", ignore = true)
    Notification toEntity(NotificationDTO dto);

    NotificationDTO toDTO(Notification entity);
}

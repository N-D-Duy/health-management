package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.account.AccountDTO;
import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.domain.entities.Account;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@org.mapstruct.Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account account);

    Account toEntity(AccountDTO accountDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "accountDTO.username", target = "username")
    @Mapping(source = "accountDTO.email", target = "email")
    @Mapping(source = "accountDTO.password", target = "password")
    @Mapping(source = "accountDTO.phone", target = "phone")
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    Account updateFromDTO(UpdateAccountRequest accountDTO, @MappingTarget Account account);
}

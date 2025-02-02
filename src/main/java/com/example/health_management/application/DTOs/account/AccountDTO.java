package com.example.health_management.application.DTOs.account;

import com.example.health_management.common.shared.enums.AccountStatus;
import lombok.Data;

/**
 * DTO for {@link com.example.health_management.domain.entities.Account}
 */
@Data
public class AccountDTO {
    private Long id;
    private String email;
    private String password;
    private String role;
    private String phone;
    private AccountStatus status;
}

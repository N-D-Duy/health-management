package com.example.health_management.application.DTOs.user.response;

import com.example.health_management.application.DTOs.account.AccountDTO;
import com.example.health_management.application.DTOs.address.response.AddressDTO;
import lombok.Data;

import java.util.List;

/**
 * DTO for {@link com.example.health_management.domain.entities.User}
 */
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String avatarUrl;
    private AccountDTO account;
    private List<AddressDTO> addresses;
}

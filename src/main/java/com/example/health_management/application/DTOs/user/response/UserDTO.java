package com.example.health_management.application.DTOs.user.response;

import com.example.health_management.application.DTOs.account.AccountDTO;
import com.example.health_management.application.DTOs.address.AddressDTO;
import com.example.health_management.application.DTOs.doctor.DoctorDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.example.health_management.domain.entities.User}
 */
@Data
public class UserDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String avatarUrl;
    private AccountDTO account;
    private Set<AddressDTO> addresses;
    private DoctorDTO doctorProfile;
}

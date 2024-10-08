package com.example.health_management.application.DTOs.user.request;

import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.DTOs.address.request.UpdateAddressRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
    private UpdateAccountRequest account;
    private Set<UpdateAddressRequest> addresses;
    private DoctorDTO doctorProfile;
}

package com.example.health_management.application.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    @NotNull(message = "Email không được null")
    @Email(message = "Email phải đúng định dạng")
    private String email;
    @NotNull(message = "Password không được null")
    @Size(min = 6, max = 20, message = "Password phải có ít nhất 6 ký tự")
    private String password;
}

package com.example.health_management.application.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginDto {
    @NotNull(message = "Email không được null")
    @Email(message = "Email phải đúng định dạng")
    private String email;

    @NotNull(message = "Password không được null")
    @Size(min = 6, max = 20, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    public LoginDto() {
    }

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

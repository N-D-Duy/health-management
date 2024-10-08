package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.domain.services.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.health_management.domain.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public @ResponseBody List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/health")
    public @ResponseBody String getHealth() {
        return "OK";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public @ResponseBody String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "Deleted";
    }

//    @PostMapping("/create-doctor/{id}")
//    public @ResponseBody DoctorDTO createDoctor(@RequestBody DoctorDTO doctorDTO, @PathVariable("id") Long id) {
//        return userService.createDoctor(doctorDTO, id);
//    }
//
//    @PostMapping("/create-address/{id}")
//    public @ResponseBody UserDTO createAddress(@RequestBody AddressRequest addressRequest, @PathVariable("id") Long id) {
//        return userService.createNewAddress(addressRequest, id);
//    }

    @PostMapping("/update-user/{id}")
    public @ResponseBody UserDTO updateUser(@RequestBody UpdateUserRequest userDTO, @PathVariable("id") Long id) {
        return userService.updateUser(userDTO, id);
    }

//    @PostMapping("/update-doctor/{id}")
//    public @ResponseBody DoctorDTO updateDoctor(@RequestBody UpdateDoctorRequest doctorDTO, @PathVariable("id") Long id) {
//        return userService.updateDoctor(doctorDTO, id);
//    }
//
//    @PostMapping("/update-account/{id}")
//    public @ResponseBody void updateAccount(@PathVariable("id") Long id, @RequestBody UpdateAccountRequest updateAccountRequest) {
//        accountService.updateAccount(id, updateAccountRequest);
//    }
//
//    @PostMapping("/update-address/{id}")
//    public @ResponseBody UserDTO updateAddress(@PathVariable("id") Long id, @RequestBody AddressRequest addressRequest) {
//        return userService.updateAddress(addressRequest, id);
//    }
}

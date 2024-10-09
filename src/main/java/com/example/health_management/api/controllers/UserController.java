package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.health_management.domain.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public @ResponseBody List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/health")
    public @ResponseBody String getHealth() {
        return "OK";
    }

    @GetMapping("/delete/{id}")
    public @ResponseBody String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "Deleted";
    }

    @GetMapping("/summary/{id}")
    public @ResponseBody UserSummaryDTO getUser(@PathVariable("id") Long id) {
        return userService.getUserSummary(id);
    }

    @PostMapping("/update-user/{id}")
    public @ResponseBody UserDTO updateUser(@RequestBody UpdateUserRequest userDTO, @PathVariable("id") Long id) {
        return userService.updateUser(userDTO, id);
    }
}

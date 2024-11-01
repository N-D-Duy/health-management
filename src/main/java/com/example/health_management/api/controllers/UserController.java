package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.common.utils.handle_privilege.CheckUserMatch;
import com.example.health_management.domain.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;
    private final CacheManager cacheManager;

    @GetMapping("/all")
    public @ResponseBody List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/health")
    public @ResponseBody String getHealth() {
        return "OK";
    }

    @CheckUserMatch(paramName = "userId")
    @GetMapping("/delete")
    public @ResponseBody String deleteUser(@Param("userId") Long userId) {
        userService.deleteById(userId);
        return "Deleted";
    }

    @GetMapping("/summary/{id}")
    public @ResponseBody UserSummaryDTO getUser(@PathVariable("id") Long id) {
        return userService.getUserSummary(id);
    }

    @CheckUserMatch(paramName = "userId")
    @PostMapping("/update-user")
    public @ResponseBody UserDTO updateUser(@RequestBody UpdateUserRequest userDTO, @Param("userId") Long userId) {
        return userService.updateUser(userDTO, userId);
    }

    @GetMapping("/email")
    public @ResponseBody ResponseEntity<ApiResponse<UserSummaryDTO>> getUserByEmail(@Param("email") String email) {
        UserSummaryDTO user = userService.getUserByEmail(email);
        ApiResponse<UserSummaryDTO> response = ApiResponse.<UserSummaryDTO>builder().code(200).data(user).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-rated")
    public @ResponseBody ResponseEntity<ApiResponse<List<DoctorDTO>>> getTopRatedDoctors() {
        List<DoctorDTO> doctors = userService.getTopRatedDoctors();
        ApiResponse<List<DoctorDTO>> response = ApiResponse.<List<DoctorDTO>>builder().code(200).data(doctors).message("Success").build();
        return ResponseEntity.ok(response);
    }
}

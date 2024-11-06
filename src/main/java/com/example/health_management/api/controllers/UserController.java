package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.common.shared.enums.DoctorAction;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.common.utils.handle_privilege.doctor_access.DoctorAccess;
import com.example.health_management.common.utils.handle_privilege.self_privilege.CheckUserMatch;
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
    public @ResponseBody ResponseEntity<ApiResponse<List<UserDTO>>> getUsers() {
        List<UserDTO> users = userService.getAllUsers();
        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder().code(200).data(users).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctors")
    public @ResponseBody ResponseEntity<ApiResponse<List<UserDTO>>> getDoctors() {
        List<UserDTO> doctors = userService.getAllDoctors();
        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder().code(200).data(doctors).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patients")
    public @ResponseBody ResponseEntity<ApiResponse<List<UserDTO>>> getPatients() {
        List<UserDTO> patients = userService.getAllPatients();
        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder().code(200).data(patients).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public @ResponseBody String getHealth() {
        return "OK";
    }

    @CheckUserMatch()
    @GetMapping("/delete-user")
    public @ResponseBody ResponseEntity<ApiResponse<String>> deleteUser(@Param("userId") Long userId) {
        try{
            userService.deleteById(userId);
            return ResponseEntity.ok(ApiResponse.<String>builder().code(200).data("Deleted").message("Success").build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.<String>builder().code(400).data("Failed").message("Failed").build());
        }
    }

    @DoctorAccess(action = DoctorAction.DELETE)
    @GetMapping("/delete-doctor")
    public @ResponseBody ResponseEntity<ApiResponse<String>> deleteDoctor(@Param("doctorId") Long doctorId) {
        try {
            userService.deleteById(doctorId);
            return ResponseEntity.ok(ApiResponse.<String>builder().code(200).data("Deleted").message("Success").build());
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }


    @GetMapping("/summary/{id}")
    public @ResponseBody ResponseEntity<ApiResponse<UserSummaryDTO>> getUser(@PathVariable("id") Long id) {
        UserSummaryDTO user = userService.getUserSummary(id);
        ApiResponse<UserSummaryDTO> response = ApiResponse.<UserSummaryDTO>builder().code(200).data(user).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @CheckUserMatch()
    @PostMapping("/update-user")
    public @ResponseBody ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody UpdateUserRequest userDTO, @Param("userId") Long userId) {
        UserDTO user = userService.updateUser(userDTO, userId, false);
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder().code(200).data(user).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @DoctorAccess(action = DoctorAction.UPDATE)
    @PostMapping("/update-doctor")
    public @ResponseBody ResponseEntity<ApiResponse<UserDTO>> updateDoctor(@RequestBody UpdateUserRequest userDTO, @Param("doctorId") Long doctorId) {
        UserDTO user = userService.updateUser(userDTO, doctorId, true);
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder().code(200).data(user).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public @ResponseBody ResponseEntity<ApiResponse<UserSummaryDTO>> getUserByEmail(@Param("email") String email) {
        UserSummaryDTO user = userService.getUserSummaryByEmail(email);
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

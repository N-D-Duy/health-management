package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.doctor.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AccountMapper;
import com.example.health_management.application.mapper.AddressMapper;
import com.example.health_management.application.mapper.DoctorMapper;
import com.example.health_management.application.mapper.UserMapper;
import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.AddressRepository;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final AccountMapper accountMapper;
    private final DoctorMapper doctorMapper;
    private final JwtProvider jwtProvider;
    private final AddressService addressService;
    private final AccountService accountService;
    private final LoggingService loggingService;

    public void deleteById(Long id) {
        try {
            if(accountRepository.findByIdActive(id) == null) {
                throw new ConflictException("User not found");
            }
            accountRepository.deleteById(id);
            loggingService.saveLog(LoggingDTO.builder().message("User with id" + id + "deleted").type(LoggingType.USER_DELETED).build());
        } catch (Exception e) {
            loggingService.saveLog(LoggingDTO.builder().message("Error deleting user with id" + id).type(LoggingType.USER_DELETED).build());
            throw new ConflictException(e.getMessage());
        }
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAllActive().stream().map(userMapper::toUserDTO).toList();
    }

    public List<UserDTO> getAllDoctors() {
        List<UserDTO> users = userRepository.findAllActive().stream().map(userMapper::toUserDTO).toList();
        users = users.stream().filter(user -> user.getAccount().getRole().equals("DOCTOR")).toList();
        return users;
    }

    public List<UserDTO> getAllPatients() {
        List<UserDTO> users = userRepository.findAllActive().stream().map(userMapper::toUserDTO).toList();
        users = users.stream().filter(user -> user.getAccount().getRole().equals("USER")).toList();
        return users;
    }

    public UserSummaryDTO getUserSummaryByEmail(String email) {
        User user = userRepository.findActiveByEmail(email);
        return userMapper.toUserSummaryDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findActiveByEmail(email);
        return userMapper.toUserDTO(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdActive(id);
        return userMapper.toUserDTO(user);
    }

    public UserDTO updateUser(UpdateUserRequest request, Long userId, Boolean isDoctorUpdate) {
        try {
            User user = userRepository.findByIdActive(userId);

            // Update Account
            if (request.getAccount() != null) {
                UpdateAccountRequest updateAccountRequest = request.getAccount();
                accountService.updateAccount(updateAccountRequest, user);
            }

            // Update Addresses
            if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
                addressService.updateAddresses(user, request.getAddresses());
            }

            if(isDoctorUpdate && request.getDoctorProfile() != null) {
                updateDoctorProfile(user, request.getDoctorProfile());
            }

            //update other fields
            userMapper.update(request, user);

            userRepository.save(user);
            loggingService.saveLog(LoggingDTO.builder().message("User with id" + userId + "updated").type(LoggingType.USER_UPDATED).build());
            return userMapper.toUserDTO(user);
        } catch (Exception e) {
            throw new ConflictException("Error updating user: " + e.getMessage());
        }
    }

    private void updateDoctorProfile(@NonNull User user, @NonNull DoctorDTO doctorDTO) {
        try {
            Doctor doctorProfile = user.getDoctorProfile();

            if (doctorProfile != null) {
                // Update existing doctor profile
                doctorMapper.updateDoctor(doctorDTO, doctorProfile);
            } else {
                // Create new doctor profile
                doctorProfile = doctorMapper.toEntity(doctorDTO);
                doctorProfile.setUser(user);
                user.setDoctorProfile(doctorProfile);
            }
            loggingService.saveLog(LoggingDTO.builder().message("Doctor profile updated for user with id" + user.getId()).type(LoggingType.DOCTOR_PROFILE_UPDATED).build());
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public UserSummaryDTO getUserSummary(Long userId) {
        User user = userRepository.findByIdActive(userId);
        return userMapper.toUserSummaryDTO(user);
    }

    public List<UserDTO> getTopRatedDoctors() {
        return userRepository.topRatedDoctors().stream().map(userMapper::toUserDTO).toList();
    }

    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream().map(doctorMapper::toDoctorDTO).toList();
    }
}

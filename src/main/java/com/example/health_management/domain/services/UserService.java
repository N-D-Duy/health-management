package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AccountMapper;
import com.example.health_management.application.mapper.AddressMapper;
import com.example.health_management.application.mapper.DoctorMapper;
import com.example.health_management.application.mapper.UserMapper;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.AddressRepository;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
@RequiredArgsConstructor
@Transactional
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


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAllActive().stream().map(userMapper::toUserDTO).toList();
    }

    public UserSummaryDTO getUserByEmail(String email) {
        User user = userRepository.findActiveByEmail(email);
        return userMapper.toUserSummaryDTO(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdActive(id);
        return userMapper.toUserDTO(user);
    }

    public UserDTO updateUser(UpdateUserRequest request, Long userId) {
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

            // Update Doctor Profile
            if (request.getDoctorProfile() != null) {
                updateDoctorProfile(user, request.getDoctorProfile());
            }

            //update other fields
            userMapper.update(request, user);

            userRepository.save(user);
            return userMapper.toUserDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    private void updateDoctorProfile(@NonNull User user, @NonNull DoctorDTO doctorDTO) {
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
    }

    public UserSummaryDTO getUserSummary(Long userId) {
        User user = userRepository.findByIdActive(userId);
        return userMapper.toUserSummaryDTO(user);
    }
}

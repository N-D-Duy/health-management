package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.doctor.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AccountMapper;
import com.example.health_management.application.mapper.AddressMapper;
import com.example.health_management.application.mapper.DoctorMapper;
import com.example.health_management.application.mapper.UserMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cache.services.UserCacheService;
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

    //cache
    private final UserCacheService userCacheService;

    public void deleteById(Long id) {
        try {
            if (accountRepository.findByIdActive(id) == null) {
                throw new ConflictException("User not found");
            }
            accountRepository.deleteById(id);

            String cacheKey = "user:" + id;
            userCacheService.cacheUser(cacheKey, null);
            userCacheService.invalidateAllUsersCache();
            userCacheService.invalidateTopRatedDoctorsCache(); // Invalidate top rated doctors cache as well
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> cachedUsers = userCacheService.getCachedAllUsers();
        if (cachedUsers != null && !cachedUsers.isEmpty()) {
            return cachedUsers;
        }

        List<UserDTO> users = userRepository.findAllActive().stream().map(userMapper::toUserDTO).toList();
        userCacheService.cacheAllUsers(users);
        return users;
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
        String cacheKey = "user:" + id;
        UserDTO cachedUser = userCacheService.getCachedUser(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        User user = userRepository.findByIdActive(id);
        UserDTO userDTO = userMapper.toUserDTO(user);
        userCacheService.cacheUser(cacheKey, userDTO);
        return userDTO;
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

            if (isDoctorUpdate && request.getDoctorProfile() != null) {
                updateDoctorProfile(user, request.getDoctorProfile());
            }

            userMapper.update(request, user);

            userRepository.save(user);

            UserDTO updatedUserDTO = userMapper.toUserDTO(user);

            String cacheKey = "user:" + userId;
            userCacheService.cacheUser(cacheKey, updatedUserDTO);
            userCacheService.invalidateAllUsersCache();
            
            // If it's a doctor update, invalidate top rated doctors cache
            if (isDoctorUpdate || "DOCTOR".equals(updatedUserDTO.getAccount().getRole())) {
                userCacheService.invalidateTopRatedDoctorsCache();
            }

            return updatedUserDTO;
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
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public UserSummaryDTO getUserSummary(Long userId) {
        User user = userRepository.findByIdActive(userId);
        return userMapper.toUserSummaryDTO(user);
    }

    public List<UserDTO> getTopRatedDoctors() {
        // Try to get from cache first
        List<UserDTO> cachedDoctors = userCacheService.getCachedTopRatedDoctors();
        if (cachedDoctors != null && !cachedDoctors.isEmpty()) {
            return cachedDoctors;
        }
        
        // If not cached, get from repository
        List<UserDTO> doctors = userRepository.topRatedDoctors().stream().map(userMapper::toUserDTO).toList();
        
        // Cache the result
        userCacheService.cacheTopRatedDoctors(doctors);
        
        return doctors;
    }

    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream().map(doctorMapper::toDoctorDTO).toList();
    }
}

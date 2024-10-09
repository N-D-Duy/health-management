package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.account.AccountDTO;
import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.DTOs.address.request.UpdateAddressRequest;
import com.example.health_management.application.DTOs.address.response.AddressDTO;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AccountMapper;
import com.example.health_management.application.mapper.AddressMapper;
import com.example.health_management.application.mapper.DoctorMapper;
import com.example.health_management.application.mapper.UserMapper;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.Address;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.AddressRepository;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
@RequiredArgsConstructor
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

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAllActive().stream().map(userMapper::toUserDTO).toList();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdActive(id);
        return userMapper.toUserDTO(user);
    }

    public UserDTO updateUser(UpdateUserRequest request, Long userId) {
        try {
            User user = userRepository.findByIdActive(userId);

            if (request.getAccount() != null) {
                UpdateAccountRequest updateAccountRequest = request.getAccount();
                Account account = accountMapper.updateFromDTO(updateAccountRequest, user.getAccount());
                user.setAccount(account);
            }

            if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
                Set<Address> existingAddresses = user.getAddresses();

                // Create a map of existing addresses by ID for easy lookup
                Map<Long, Address> existingAddressMap = existingAddresses.stream()
                        .collect(Collectors.toMap(Address::getId, address -> address));

                for (UpdateAddressRequest addressRequest : request.getAddresses()) {
                    Address address;

                    if (addressRequest.getId() != null) {
                        // Update existing address
                        address = existingAddressMap.get(addressRequest.getId());
                        if (address != null) {
                            addressMapper.updateAddress(addressRequest, address);
                            existingAddresses.add(address);
                        } else {
                            // Handle case where ID is provided but address doesn't exist
                            throw new RuntimeException("Address with ID " + addressRequest.getId() + " not found");
                        }
                    } else {
                        // Create new address
                        Address newAddress = addressMapper.toEntity(addressMapper.toDTOFromRequest(addressRequest));
                        newAddress.setUser(user);
                        existingAddresses.add(newAddress);
                    }
                }
                // Update user's addresses
                user.setAddresses(existingAddresses);
            }

            if (request.getDoctorProfile() != null) {
                if(user.getDoctorProfile()!=null){
                    //update doctor
                    DoctorDTO doctorDTO = request.getDoctorProfile();
                    Doctor doctor=doctorMapper.updateDoctor(doctorDTO, user.getDoctorProfile());
                    user.setDoctorProfile(doctor);
                } else{
                    //tao moi doctor
                    Doctor doctor = doctorMapper.toEntity(request.getDoctorProfile());
                    doctor.setUser(user);
                    user.setDoctorProfile(doctor);
                }
            }

            userRepository.save(user);
            return userMapper.toUserDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    public UserSummaryDTO getUserSummary(Long userId) {
        User user = userRepository.findByIdActive(userId);
        return userMapper.toUserSummaryDTO(user);
    }
}

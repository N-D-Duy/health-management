package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.user.request.UpdateDoctorRequest;
import com.example.health_management.application.DTOs.user.request.UpdateUserRequest;
import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.application.mapper.UserMapper;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.doctorRepository = doctorRepository;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Map<String, List<?>> getAllUsers() {
        List<User> allUsers = userRepository.findAllActive();

        List<UserDTO> regularUsers = new ArrayList<>();
        List<DoctorDTO> doctors = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getDoctorProfile() != null) {
                doctors.add(userMapper.toDoctorDTO(user));
            } else {
                regularUsers.add(userMapper.toUserDTO(user));
            }
        }

        return Map.of(
                "regularUsers", regularUsers,
                "doctors", doctors
        );
    }

    public List<DoctorDTO> getDoctors() {
        List<User> allUsers = userRepository.findAllActive();
        return allUsers.stream()
                .filter(user -> user.getDoctorProfile() != null)
                .map(userMapper::toDoctorDTO)
                .toList();
    }

    public List<UserDTO> getUsers() {
        List<User> allUsers = userRepository.findAllActive();
        return allUsers.stream()
                .filter(user -> user.getDoctorProfile() == null)
                .map(userMapper::toUserDTO)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdActive(id);
        return userMapper.toUserDTO(user);
    }

    public DoctorDTO getDoctorById(Long id) {
        User user = userRepository.findByIdActive(id);
        return userMapper.toDoctorDTO(user);
    }

    public DoctorDTO createDoctor(DoctorDTO doctorDTO, Long userId) {
        try{
            Doctor doctor = userMapper.toDoctorEntity(doctorDTO);

            //update doctor profile in user table
            User user = userRepository.findByIdActive(userId);
            user.setDoctorProfile(doctor);

            doctor.setUser(user);
            doctorRepository.save(doctor);

            return userMapper.toDoctorDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating doctor");
        }
    }

    public UserDTO updateUser(UpdateUserRequest request, Long userId) {
        try {
            User user = userMapper.updateUserFromDTO(request, userRepository.findByIdActive(userId));
            userRepository.save(user);
            return userMapper.toUserDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user");
        }
    }

    public DoctorDTO updateDoctor(UpdateDoctorRequest request, Long userId) {
        try {
            User existingUser = userRepository.findByIdActive(userId);
            Doctor existingDoctor = existingUser.getDoctorProfile();
            Doctor updatedDoctor = userMapper.updateDoctorFromDTO(request, existingDoctor);
            doctorRepository.save(updatedDoctor);
            return userMapper.toDoctorDTO(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating doctor");
        }
    }
}

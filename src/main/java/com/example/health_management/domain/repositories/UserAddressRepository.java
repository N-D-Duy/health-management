package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
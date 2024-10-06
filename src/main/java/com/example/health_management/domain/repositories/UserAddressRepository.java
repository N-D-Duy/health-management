package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.UserAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends SoftDeleteRepository<UserAddress, Long> {
}
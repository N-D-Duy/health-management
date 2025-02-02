package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends SoftDeleteRepository<Address, Long> {
}
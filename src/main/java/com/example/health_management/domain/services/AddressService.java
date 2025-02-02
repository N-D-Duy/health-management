package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.address.AddressDTO;
import com.example.health_management.application.mapper.AddressMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Address;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AddressRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    protected void updateAddresses(@NonNull User user, @NonNull Set<AddressDTO> addressDTOs) {
        Set<Address> existingAddresses = user.getAddresses();
        Map<Long, Address> existingAddressMap = existingAddresses.stream()
                .collect(Collectors.toMap(Address::getId, address -> address));

        Set<Address> updatedAddresses = new HashSet<>();

        for (AddressDTO addressDTO : addressDTOs) {
            if (addressDTO.getId() != null) {
                Address existingAddress = existingAddressMap.get(addressDTO.getId());
                if (existingAddress == null) {
                    throw new ConflictException("Address with ID " + addressDTO.getId() + " not found");
                }
                addressMapper.updateAddress(addressDTO, existingAddress);
                updatedAddresses.add(existingAddress);
            } else {
                Address newAddress = addressMapper.toEntity(addressDTO);
                newAddress.setUser(user);
                updatedAddresses.add(newAddress);
            }
        }

        user.setAddresses(updatedAddresses);
    }
}

package com.example.health_management.domain.services;


import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.repositories.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyService {

    private final KeyRepository keyRepository;

    public Key findKeyByUserId(Integer userId) {
        return keyRepository.findKeyByUserId(userId);
    }

}

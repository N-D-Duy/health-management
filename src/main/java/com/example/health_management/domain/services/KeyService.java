package com.example.health_management.domain.services;


import com.example.health_management.domain.repositories.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyService {
    @Autowired
    private KeyRepository keyRepository;

    public KeyService(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public String findPublicKeyByUserId(Integer userId) {
        return keyRepository.findPublicKeyByUserId(userId);
    }
}

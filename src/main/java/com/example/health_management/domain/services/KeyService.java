package com.example.health_management.domain.services;


import org.springframework.stereotype.Service;

import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.repositories.KeyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KeyService {

    private final KeyRepository keyRepository;

    public Key findKeyByUserId(Long userId) {
        return keyRepository.findKeyByUserId(userId);
    }

    public void updateVersion(Long userId, int version) {
        Key key = keyRepository.findKeyByUserId(userId);
        if(version == Integer.MAX_VALUE) {
            version = 0;
        }
        key.setVersion(version);
        keyRepository.save(key);
    }

    public void updateKey(Long userId, String fcmToken) {
        Key userKey = keyRepository.findKeyByUserId(userId);
        userKey.setNotificationKey(fcmToken);
        keyRepository.save(userKey);
    }
}

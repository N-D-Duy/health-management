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

    public void updateVersion(Integer userId, int version) {
        Key key = keyRepository.findKeyByUserId(userId);
        if(version == Integer.MAX_VALUE){
            version = 0;
        }
        key.setVersion(version);
        keyRepository.save(key);
    }

    public void updateKey(Integer userId, String fcmToken) {
        Key userKey = keyRepository.findKeyByUserId(userId);
        userKey.setNotificationKey(fcmToken);
        keyRepository.save(userKey);
    }
}

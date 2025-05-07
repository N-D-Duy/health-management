package com.example.health_management.domain.cache.services;

import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Duration TTL = Duration.ofHours(1);
    
    private static final String ALL_USERS_KEY = "all:users";
    private static final String TOP_RATED_DOCTORS_KEY = "doctor:top_rated";
    private static final String USER_KEY_PREFIX = "user:";

    public void cacheAllUsers(List<UserDTO> users) {
        if (users == null) {
            redisTemplate.delete(ALL_USERS_KEY);
            return;
        }
        
        try {
            String usersJson = objectMapper.writeValueAsString(users);
            redisTemplate.opsForValue().set(ALL_USERS_KEY, usersJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache all users", e);
        }
    }

    public List<UserDTO> getCachedAllUsers() {
        try {
            return getUserDTOS(ALL_USERS_KEY);
        } catch (Exception e) {
            log.error("Failed to retrieve cached users", e);
            invalidateAllUsersCache();
            return null;
        }
    }

    public void cacheUser(String userId, UserDTO user) {
        if (user == null) {
            redisTemplate.delete(userId);
            return;
        }
        
        try {
            String cacheKey = USER_KEY_PREFIX + userId;
            String userJson = objectMapper.writeValueAsString(user);
            redisTemplate.opsForValue().set(cacheKey, userJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache user: {}", userId, e);
        }
    }

    public UserDTO getCachedUser(String userId) {
        try {
            String cacheKey = USER_KEY_PREFIX + userId;
            Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, UserDTO.class);
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve cached user: {}", userId, e);
            redisTemplate.delete(userId);
            return null;
        }
    }
    
    public void invalidateAllUsersCache() {
        redisTemplate.delete(ALL_USERS_KEY);
    }

    public void cacheTopRatedDoctors(List<UserDTO> doctors) {
        if (doctors == null) {
            redisTemplate.delete(TOP_RATED_DOCTORS_KEY);
            return;
        }
        
        try {
            String doctorsJson = objectMapper.writeValueAsString(doctors);
            redisTemplate.opsForValue().set(TOP_RATED_DOCTORS_KEY, doctorsJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache top rated doctors", e);
        }
    }

    public List<UserDTO> getCachedTopRatedDoctors() {
        try {
            return getUserDTOS(TOP_RATED_DOCTORS_KEY);
        } catch (Exception e) {
            log.error("Failed to retrieve cached top rated doctors", e);
            invalidateTopRatedDoctorsCache();
            return null;
        }
    }

    private List<UserDTO> getUserDTOS(String topRatedDoctorsKey) throws com.fasterxml.jackson.core.JsonProcessingException {
        Object cachedValue = redisTemplate.opsForValue().get(topRatedDoctorsKey);
        if (cachedValue == null) {
            return null;
        }

        if (cachedValue instanceof String) {
            return objectMapper.readValue((String) cachedValue, new TypeReference<List<UserDTO>>() {});
        }

        return null;
    }

    public void invalidateTopRatedDoctorsCache() {
        redisTemplate.delete(TOP_RATED_DOCTORS_KEY);
    }
}

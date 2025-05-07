package com.example.health_management.domain.cache.services;

import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
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
public class AppointmentCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Duration TTL = Duration.ofHours(1);
    
    private static final String APPOINTMENT_KEY_PREFIX = "appointment:";
    private static final String USER_APPOINTMENTS_KEY_PREFIX = "user:appointments:";
    private static final String DOCTOR_APPOINTMENTS_KEY_PREFIX = "doctor:appointments:";
    private static final String ALL_APPOINTMENTS_KEY = "all:appointments";

    public void cacheAppointment(Long appointmentId, AppointmentRecordDTO appointment) {
        try {
            String cacheKey = APPOINTMENT_KEY_PREFIX + appointmentId;
            if (appointment == null) {
                redisTemplate.delete(cacheKey);
                return;
            }
            String appointmentJson = objectMapper.writeValueAsString(appointment);
            redisTemplate.opsForValue().set(cacheKey, appointmentJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache appointment: {}", appointmentId, e);
        }
    }
    
    public AppointmentRecordDTO getCachedAppointment(Long appointmentId) {
        try {
            String cacheKey = APPOINTMENT_KEY_PREFIX + appointmentId;
            Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, AppointmentRecordDTO.class);
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve cached appointment: {}", appointmentId, e);
            redisTemplate.delete(APPOINTMENT_KEY_PREFIX + appointmentId);
            return null;
        }
    }
    
    public void cacheUserAppointments(Long userId, List<AppointmentRecordDTO> appointments) {
        try {
            String cacheKey = USER_APPOINTMENTS_KEY_PREFIX + userId;
            if (appointments == null || appointments.isEmpty()) {
                redisTemplate.delete(cacheKey);
                return;
            }
            String appointmentsJson = objectMapper.writeValueAsString(appointments);
            redisTemplate.opsForValue().set(cacheKey, appointmentsJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache user appointments: {}", userId, e);
        }
    }
    
    public List<AppointmentRecordDTO> getCachedUserAppointments(Long userId) {
        try {
            String cacheKey = USER_APPOINTMENTS_KEY_PREFIX + userId;
            Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, new TypeReference<List<AppointmentRecordDTO>>() {});
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve cached user appointments: {}", userId, e);
            redisTemplate.delete(USER_APPOINTMENTS_KEY_PREFIX + userId);
            return null;
        }
    }
    
    public void cacheDoctorAppointments(Long doctorId, List<AppointmentRecordDTO> appointments) {
        try {
            String cacheKey = DOCTOR_APPOINTMENTS_KEY_PREFIX + doctorId;
            if (appointments == null || appointments.isEmpty()) {
                redisTemplate.delete(cacheKey);
                return;
            }
            String appointmentsJson = objectMapper.writeValueAsString(appointments);
            redisTemplate.opsForValue().set(cacheKey, appointmentsJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache doctor appointments: {}", doctorId, e);
        }
    }
    
    public List<AppointmentRecordDTO> getCachedDoctorAppointments(Long doctorId) {
        try {
            String cacheKey = DOCTOR_APPOINTMENTS_KEY_PREFIX + doctorId;
            Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, new TypeReference<List<AppointmentRecordDTO>>() {});
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve cached doctor appointments: {}", doctorId, e);
            redisTemplate.delete(DOCTOR_APPOINTMENTS_KEY_PREFIX + doctorId);
            return null;
        }
    }
    
    public void cacheAllAppointments(List<AppointmentRecordDTO> appointments) {
        try {
            if (appointments == null || appointments.isEmpty()) {
                redisTemplate.delete(ALL_APPOINTMENTS_KEY);
                return;
            }
            String appointmentsJson = objectMapper.writeValueAsString(appointments);
            redisTemplate.opsForValue().set(ALL_APPOINTMENTS_KEY, appointmentsJson, TTL);
        } catch (Exception e) {
            log.error("Failed to cache all appointments", e);
        }
    }
    
    public List<AppointmentRecordDTO> getCachedAllAppointments() {
        try {
            Object cachedValue = redisTemplate.opsForValue().get(ALL_APPOINTMENTS_KEY);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, new TypeReference<List<AppointmentRecordDTO>>() {});
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve all cached appointments", e);
            redisTemplate.delete(ALL_APPOINTMENTS_KEY);
            return null;
        }
    }
    
    public void invalidateAppointmentCaches(Long appointmentId, Long userId, Long doctorId) {
        redisTemplate.delete(APPOINTMENT_KEY_PREFIX + appointmentId);
        redisTemplate.delete(USER_APPOINTMENTS_KEY_PREFIX + userId);
        redisTemplate.delete(DOCTOR_APPOINTMENTS_KEY_PREFIX + doctorId);
        redisTemplate.delete(ALL_APPOINTMENTS_KEY);
    }
}

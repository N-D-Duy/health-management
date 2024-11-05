package com.example.health_management.domain.services;

import com.example.health_management.domain.entities.Notification;
import com.example.health_management.domain.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}

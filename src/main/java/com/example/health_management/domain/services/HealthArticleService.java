package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.health_article.HealthArticleDTO;
import com.example.health_management.application.mapper.HealthArticleMapper;
import com.example.health_management.domain.entities.HealthArticle;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.HealthArticleRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HealthArticleService {
    private final HealthArticleRepository healthArticleRepository;
    private final HealthArticleMapper healthArticleMapper;
    private final UserRepository userRepository;

    public HealthArticleDTO createHealthArticle(HealthArticleDTO healthArticleDTO, Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + healthArticleDTO.getUserId()));

            // Map DTO to entity and set the user
            HealthArticle healthArticle = healthArticleMapper.toEntity(healthArticleDTO);
            healthArticle.setUser(user);

            return healthArticleMapper.toDTO(healthArticleRepository.save(healthArticle));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create health article");
        }
    }

    public HealthArticleDTO updateHealthArticle(HealthArticleDTO healthArticleDTO, Long userId) {
        try {
            return healthArticleMapper
                    .toDTO(healthArticleRepository.save(healthArticleMapper.updateFromDTO(healthArticleDTO, healthArticleRepository.findById(healthArticleDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Health article not found with ID: " + healthArticleDTO.getId())))));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update health article");
        }
    }

    public List<HealthArticleDTO> getHealthArticles() {
        try {
            return healthArticleRepository.findAllActive().stream()
                    .map(healthArticleMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get health articles");
        }
    }

    public List<HealthArticleDTO> getHealthArticlesByUserId(Long userId) {
        try {
            return healthArticleRepository.findAllByUserId(userId).stream()
                    .map(healthArticleMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get health articles by user id");
        }
    }

    public void deleteHealthArticle(Long id) {
        try {
            healthArticleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete health article");
        }
    }
}

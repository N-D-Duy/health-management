package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.health_article.HealthArticleDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.common.utils.handle_privilege.CheckUserMatch;
import com.example.health_management.domain.services.HealthArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/health-articles")
@RequiredArgsConstructor
public class HealthArticleController {
    private final HealthArticleService healthArticleService;

    @CheckUserMatch
    @PostMapping("/create")
    public @ResponseBody ResponseEntity<ApiResponse<HealthArticleDTO>> createHealthArticle(@RequestBody HealthArticleDTO healthArticleDTO, @Param("userId") Long userId) {
        HealthArticleDTO healthArticle = healthArticleService.createHealthArticle(healthArticleDTO, userId);
        ApiResponse<HealthArticleDTO> response = ApiResponse.<HealthArticleDTO>builder().code(HttpStatus.OK.value()).data(healthArticle).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @CheckUserMatch
    @PostMapping("/update")
    public @ResponseBody ResponseEntity<ApiResponse<HealthArticleDTO>> updateHealthArticle(@RequestBody HealthArticleDTO healthArticleDTO, @Param("userId") Long userId) {
        HealthArticleDTO healthArticle = healthArticleService.updateHealthArticle(healthArticleDTO, userId);
        ApiResponse<HealthArticleDTO> response = ApiResponse.<HealthArticleDTO>builder().code(HttpStatus.OK.value()).data(healthArticle).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public @ResponseBody ResponseEntity<ApiResponse<List<HealthArticleDTO>>> getHealthArticles() {
        List<HealthArticleDTO> healthArticles = healthArticleService.getHealthArticles();
        ApiResponse<List<HealthArticleDTO>> response = ApiResponse.<List<HealthArticleDTO>>builder().code(HttpStatus.OK.value()).data(healthArticles).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-user-id")
    public @ResponseBody ResponseEntity<ApiResponse<List<HealthArticleDTO>>> getHealthArticlesByUserId(@Param("userId") Long userId) {
        List<HealthArticleDTO> healthArticles = healthArticleService.getHealthArticlesByUserId(userId);
        ApiResponse<List<HealthArticleDTO>> response = ApiResponse.<List<HealthArticleDTO>>builder().code(HttpStatus.OK.value()).data(healthArticles).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public @ResponseBody ResponseEntity<ApiResponse<String>> deleteHealthArticle(@Param("id") Long id) {
        healthArticleService.deleteHealthArticle(id);
        ApiResponse<String> response = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data("Success").message("Success").build();
        return ResponseEntity.ok(response);
    }
}

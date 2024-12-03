package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.example.health_management.application.DTOs.article.CreateArticleRequest;
import com.example.health_management.application.DTOs.article.UpdateArticleRequest;
import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.common.shared.enums.VoteType;
import com.example.health_management.common.utils.handle_privilege.self_privilege.CheckUserMatch;
import com.example.health_management.domain.services.ArticleCommentService;
import com.example.health_management.domain.services.ArticleService;
import com.example.health_management.domain.services.ArticleVoteService;
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
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleVoteService articleVoteService;
    private final ArticleCommentService articleCommentService;

    @CheckUserMatch
    @PostMapping("/create")
    public @ResponseBody ResponseEntity<ApiResponse<ArticleDTO>> createHealthArticle(@RequestBody CreateArticleRequest articleDTO, @Param("userId") Long userId) {
        ArticleDTO healthArticle = articleService.createArticle(articleDTO, userId);
        ApiResponse<ArticleDTO> response = ApiResponse.<ArticleDTO>builder().code(HttpStatus.OK.value()).data(healthArticle).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @CheckUserMatch
    @PostMapping("/update")
    public @ResponseBody ResponseEntity<ApiResponse<ArticleDTO>> updateHealthArticle(@RequestBody UpdateArticleRequest articleDTO, @Param("userId") Long userId) {
        ArticleDTO healthArticle = articleService.updateArticle(articleDTO, articleDTO.getId());
        ApiResponse<ArticleDTO> response = ApiResponse.<ArticleDTO>builder().code(HttpStatus.OK.value()).data(healthArticle).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public @ResponseBody ResponseEntity<ApiResponse<List<ArticleDTO>>> getHealthArticles() {
        List<ArticleDTO> healthArticles = articleService.getArticles();
        ApiResponse<List<ArticleDTO>> response = ApiResponse.<List<ArticleDTO>>builder().code(HttpStatus.OK.value()).data(healthArticles).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-user-id")
    public @ResponseBody ResponseEntity<ApiResponse<List<ArticleDTO>>> getHealthArticlesByUserId(@Param("userId") Long userId) {
        List<ArticleDTO> healthArticles = articleService.getArticlesByUserId(userId);
        ApiResponse<List<ArticleDTO>> response = ApiResponse.<List<ArticleDTO>>builder().code(HttpStatus.OK.value()).data(healthArticles).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id")
    public @ResponseBody ResponseEntity<ApiResponse<ArticleDTO>> getHealthArticleById(@Param("id") Long id) {
        ArticleDTO healthArticle = articleService.getArticle(id);
        ApiResponse<ArticleDTO> response = ApiResponse.<ArticleDTO>builder().code(HttpStatus.OK.value()).data(healthArticle).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @CheckUserMatch
    @DeleteMapping("/delete")
    public @ResponseBody ResponseEntity<ApiResponse<String>> deleteHealthArticle(@Param("userId") Long userId, @Param("id") Long id) {
        articleService.deleteArticle(id);
        ApiResponse<String> response = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data("Success").message("Success").build();
        return ResponseEntity.ok(response);
    }

//    @CheckUserMatch
    @PostMapping("/vote")
    public @ResponseBody ResponseEntity<ApiResponse<String>> voteHealthArticle(@Param("userId") Long userId, @Param("articleId") Long articleId, @Param("voteType") VoteType voteType) {
        articleVoteService.vote(articleId, userId, voteType);
        ApiResponse<String> response = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data("Success").message("Success").build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment")
    public @ResponseBody ResponseEntity<ApiResponse<ArticleCommentDTO>> commentHealthArticle(@Param("articleId") Long articleId, @Param("userId") Long userId, @RequestBody ArticleCommentDTO commentDTO) {
        ArticleCommentDTO articleCommentDTO = articleCommentService.addComment(articleId, userId, commentDTO);
        ApiResponse<ArticleCommentDTO> response = ApiResponse.<ArticleCommentDTO>builder().code(HttpStatus.OK.value()).data(articleCommentDTO).message("Success").build();
        return ResponseEntity.ok(response);
    }
}

package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.article_support.ArticleVoteDTO;
import com.example.health_management.domain.entities.ArticleVote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleVoteMapper {
    @Mapping(target = "username", source = "user.firstName")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "articleId", source = "article.id")
    ArticleVoteDTO toDTO(ArticleVote articleVote);

    @Mapping(target = "article.id", source = "articleId")
    @Mapping(target = "user.id", source = "userId")
    ArticleVote toEntity(ArticleVoteDTO articleVoteDTO);
}

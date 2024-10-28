package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.example.health_management.application.DTOs.article.CreateArticleRequest;
import com.example.health_management.application.DTOs.article.UpdateArticleRequest;
import com.example.health_management.domain.entities.Article;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {ArticleCommentMapper.class, ArticleVoteMapper.class, ArticleMediaMapper.class})
public interface ArticleMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.firstName")
    @Mapping(target = "userAvatar", source = "user.avatarUrl")
    @Mapping(target = "media", source = "media")
    ArticleDTO toDTO(Article article);

    @Mapping(target = "user.id", source = "userId")
    Article toEntity(ArticleDTO articleDTO);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "media", ignore = true)
    @Mapping(target = "upVoteCount", constant = "0")
    @Mapping(target = "downVoteCount", constant = "0")
    @Mapping(target = "commentCount", constant = "0")
    @Mapping(target = "viewCount", constant = "0")
    Article createFromRequest(CreateArticleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "votes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "upVoteCount", ignore = true)
    @Mapping(target = "downVoteCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    Article updateFromRequest(UpdateArticleRequest request, @MappingTarget Article article);
}

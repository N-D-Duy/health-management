package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.domain.entities.ArticleComment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArticleCommentMapper {
    @Mapping(target = "articleId", source = "article.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.firstName")
    @Mapping(target = "userAvatar", source = "user.avatarUrl")
    @Mapping(target = "parentId", source = "parentComment.id")
    ArticleCommentDTO toDTO(ArticleComment articleComment);

    @Mapping(target = "article.id", source = "articleId")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "parentComment.id", source = "parentId")
    ArticleComment toEntity(ArticleCommentDTO articleCommentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    ArticleComment update(ArticleCommentDTO articleCommentDTO, @MappingTarget ArticleComment articleComment);
}

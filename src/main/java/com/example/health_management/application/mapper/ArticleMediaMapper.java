package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.article_support.ArticleMediaDTO;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.ArticleMedia;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArticleMediaMapper {
    @Mapping(target = "articleId", source = "article.id")
    ArticleMediaDTO toDTO(ArticleMedia articleMedia);

    @Mapping(target = "article", source = "article")
    @Mapping(target = "id", ignore = true)
    ArticleMedia toEntity(ArticleMediaDTO articleMediaDTO, Article article);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "article", ignore = true)
    ArticleMedia updateFromDTO(ArticleMediaDTO articleMediaDTO, @MappingTarget ArticleMedia articleMedia);
}

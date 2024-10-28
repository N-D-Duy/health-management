package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.health_article.HealthArticleDTO;
import com.example.health_management.domain.entities.HealthArticle;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HealthArticleMapper {
    HealthArticle toEntity(HealthArticleDTO healthArticleDTO);

    @Mapping(target = "userId", source = "user.id")
    HealthArticleDTO toDTO(HealthArticle healthArticle);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    HealthArticle updateFromDTO(HealthArticleDTO healthArticleDTO, @MappingTarget HealthArticle healthArticle);
}

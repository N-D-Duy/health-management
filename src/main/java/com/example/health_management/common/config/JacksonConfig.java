package com.example.health_management.common.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    //todo: config snake_case for api responses/requests later on
//
//    @Bean
//    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        return builder;
//    }
//
//    @Override
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        return builder.build();
//    }
}
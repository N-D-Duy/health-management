package com.example.health_management.domain.redis;

import io.micrometer.common.lang.Nullable;
import org.slf4j.Logger;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

public enum MyCustomTtlFunction implements RedisCacheWriter.TtlFunction {

    INSTANCE;

    @Override
    public Duration getTimeToLive(Object key, @Nullable Object value) {
        return Duration.ofSeconds(30);
    }
}
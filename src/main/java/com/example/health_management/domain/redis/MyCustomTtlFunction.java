package com.example.health_management.domain.redis;

import io.micrometer.common.lang.Nullable;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

public enum MyCustomTtlFunction implements RedisCacheWriter.TtlFunction {

    INSTANCE;

    @Override
    public Duration getTimeToLive(Object key, @Nullable Object value) {
        if (key instanceof String keyStr) {
            if (keyStr.startsWith("health-management:")) {
                return Duration.ofMinutes(5);
            }
        }
        return Duration.ofMinutes(1);
    }
}
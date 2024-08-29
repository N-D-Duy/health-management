package com.example.health_management.application.guards;

import com.example.health_management.common.Constants;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        String requestMethod = request.getMethod();

        // Create or retrieve the bucket for this client
        Bucket bucket = buckets.computeIfAbsent(clientIP, k -> createBucket());

        // Try to consume a token from the bucket
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - rate limit exceeded.");
        }
    }

    private Bucket createBucket() {
        Bandwidth limit =
                Bandwidth.builder().capacity(10).refillIntervally(5, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }
}

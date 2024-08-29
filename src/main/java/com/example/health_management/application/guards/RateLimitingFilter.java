package com.example.health_management.application.guards;

import com.example.health_management.common.Constants;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        String requestMethod = request.getMethod();

        // Create or retrieve the bucket for this client
        Bucket bucket = buckets.computeIfAbsent(clientIP, k -> createBucket(requestMethod));

        // Try to consume a token from the bucket
        if (bucket.tryConsume(getCostForMethod(requestMethod))) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - rate limit exceeded.");
        }
    }

    private Bucket createBucket(String method) {
        return Bucket.builder().addLimit(Bandwidth.builder().capacity(1000).refillIntervally(1000, Duration.ofMinutes(1)).build()).build();
    }

    /*
    * POST, PUT, DELETE cost 5 tokens mean 200 requests per minute
    * GET, HEAD, OPTIONS, TRACE cost 1 token mean 1000 requests per minute
    * */
    private long getCostForMethod(String method) {
        return switch (method) {
            case "POST", "PUT", "DELETE" -> 5;
            default -> 1;
        };
    }
}

package com.example.health_management.infrastructure.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class MailClient {

    private final WebClient webClient;

    public MailClient(@Qualifier("mailWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> sendMail(String email, String content) {
        Map<String, String> body = Map.of("email", email, "content", content);
        return webClient.post()
                .uri("/api/v1/mail/send_custom")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class);
    }
}


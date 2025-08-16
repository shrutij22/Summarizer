package com.example.summarizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class TogetherService {

    private final WebClient webClient; // For HTTP requests
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON parsing

    public TogetherService(@Value("${together.api.key}") String apiKey,
                           WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.together.xyz")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> generateScheduleFromThought(String userThought) {
        // Build request body
        Map<String, Object> requestBody = Map.of(
                "model", "meta-llama/Llama-3-8b-chat-hf",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "You are a meeting summarizer. Use transcript or prompt to create a structured summary in bullet points."),
                        Map.of("role", "user", "content", userThought)
                ),
                "temperature", 0.7,
                "top_p", 0.9
        );

        // Call Together API
        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.out.println("❌ Error from Together API: " + errorBody);
                                    return Mono.error(new RuntimeException("Together API error: " + errorBody));
                                }))
                .bodyToMono(String.class)
                .map(responseBody -> {
                    try {
                        JsonNode root = objectMapper.readTree(responseBody);
                        JsonNode choices = root.get("choices");
                        if (choices != null && choices.isArray() && choices.size() > 0) {
                            return choices.get(0).get("message").get("content").asText();
                        } else {
                            throw new RuntimeException("No choices returned from Together API");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse Together API response", e);
                    }
                });
    }
}

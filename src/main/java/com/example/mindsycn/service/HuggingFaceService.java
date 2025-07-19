package com.example.mindsycn.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Service
public class HuggingFaceService {

    private final WebClient webClient;


    public HuggingFaceService( @Value("${huggingface.api.token}") String apiToken,
                               WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api-inference.huggingface.co/models/facebook/bart-large-cnn")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .build();
    }

    public Mono<String> generateText(String prompt) {
        Map<String, Object> requestBody = Collections.singletonMap("inputs", prompt);

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.out.println("❌ Error from Hugging Face: " + errorBody);
                                    return Mono.error(new RuntimeException("HF error: " + errorBody));
                                }))
                .bodyToMono(String.class)
                .map(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode root = objectMapper.readTree(responseBody);
                        return root.get(0).get("generated_text").asText(); // Extracts generated_text
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse Hugging Face response", e);
                    }
                });
    }




    public static class HuggingFaceResponse {
        private String generated_text;

        public String getGenerated_text() {
            return generated_text;
        }

        public void setGenerated_text(String generated_text) {
            this.generated_text = generated_text;
        }
    }
}


package com.example.summarizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean

    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
/* use webClient to call an external API:webClient.get()
    .uri("https://api.example.com/data")
    .retrieve()
    .bodyToMono(String.class)
    .subscribe(System.out::println);*/

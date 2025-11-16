package com.begliak.cryptocurrency.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

  private final ApiCoinloreUrlProperties apiCoinloreUrlProperties;

  @Bean
  WebClient webClient() {
    return WebClient.builder()
        .baseUrl(apiCoinloreUrlProperties.baseUrl())
        .build();
  }
}

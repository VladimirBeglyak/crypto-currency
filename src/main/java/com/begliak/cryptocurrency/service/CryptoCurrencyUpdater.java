package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoCurrencyUpdater {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final CryptoCurrencyRepository cryptoCurrencyRepository;

  public CryptoCurrency update(String url) {
    return webClient.get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class)
        .map(this::convertResponse)
        .map(convertedResponse -> convertedResponse.get(0))
        .doOnNext(convertedResponse -> log.info("Response from server: {}", convertedResponse))
        .flatMap(this::saveCryptCurrency)
        .block();
  }

  private Mono<CryptoCurrency> saveCryptCurrency(CryptResponse cryptResponse) {
    return Mono.fromCallable(() -> {
          CryptoCurrency cryptoCurrency = CryptoCurrency.builder()
              .id(Long.valueOf(cryptResponse.id()))
              .symbol(cryptResponse.symbol())
              .priceUsd(BigDecimal.valueOf(cryptResponse.priceUsd()))
              .build();
          return cryptoCurrencyRepository.save(cryptoCurrency);
        })
        .subscribeOn(Schedulers.boundedElastic());
  }

  private List<CryptResponse> convertResponse(String response) {
    try {
      return objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public record CryptResponse(
      @JsonProperty("id")
      String id,
      @JsonProperty("symbol")
      String symbol,
      @JsonProperty("name")
      String name,
      @JsonProperty("nameid")
      String nameId,
      @JsonProperty("rank")
      int rank,
      @JsonProperty("price_usd")
      double priceUsd,
      @JsonProperty("percent_change_24h")
      double percentChange24h,
      @JsonProperty("percent_change_1h")
      double percentChange1h,
      @JsonProperty("percent_change_7d")
      double percentChange7d,
      @JsonProperty("market_cap_usd")
      double marketCapitalisationUsd,
      @JsonProperty("volume24")
      double volumeOfCoinsLast24h,
      @JsonProperty("volume24a")
      double volumeOfCoinsTraded,
      @JsonProperty("csupply")
      double circulatingSupply,
      @JsonProperty("tsupply")
      double totalSupply,
      @JsonProperty("msupply")
      double maximumSupply
  ) {

  }
}

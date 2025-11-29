package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.dto.CryptResponse;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import com.begliak.cryptocurrency.service.CryptoCurrencyUpdateTask.CryptoCurrencyEventModel;
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

  public CryptoCurrencyEventModel update(String url) {
    return webClient.get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class)
        .map(this::convertResponse)
        .map(List::getFirst)
        .doOnNext(convertedResponse -> log.info("Response from server: {}", convertedResponse))
        .flatMap(this::saveCryptCurrency)
        .block();
  }

  private Mono<CryptoCurrencyEventModel> saveCryptCurrency(CryptResponse cryptResponse) {
    return Mono.fromCallable(() -> saveOrUpdate(cryptResponse))
        .map(cryptoCurrency -> new CryptoCurrencyEventModel(cryptoCurrency.getSymbol(), cryptoCurrency.getPriceUsd()))
        .subscribeOn(Schedulers.boundedElastic());
  }

  private CryptoCurrency saveOrUpdate(CryptResponse cryptResponse) {
    return cryptoCurrencyRepository.findBySymbol(cryptResponse.symbol())
        .map(cryptoCurrency -> {
          cryptoCurrency.setPriceUsd(BigDecimal.valueOf(cryptResponse.priceUsd()));
          cryptoCurrencyRepository.save(cryptoCurrency);
          return cryptoCurrency;
        })
        .orElseGet(() -> {
          CryptoCurrency cryptoCurrency = CryptoCurrency.builder()
              .externalId(Long.valueOf(cryptResponse.id()))
              .symbol(cryptResponse.symbol())
              .priceUsd(BigDecimal.valueOf(cryptResponse.priceUsd()))
              .build();
          return cryptoCurrencyRepository.save(cryptoCurrency);
        });
  }

  private List<CryptResponse> convertResponse(String response) {
    try {
      return objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

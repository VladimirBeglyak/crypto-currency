package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.ClientRepository;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

  @Value("${api.coinlore.url.btc}")
  private String actualInfoBtc;
  @Value("${api.coinlore.url.eth}")
  private String actualInfoEth;
  @Value("${api.coinlore.url.sol}")
  private String actualInfoSol;
  private static final BigDecimal MAX_CHANGE = BigDecimal.valueOf(1);
  private static final BigDecimal CONVERT_TO_PERCENT = BigDecimal.valueOf(100);

  private final CryptoCurrencyRepository cryptoCurrencyRepository;
  private final ClientRepository clientRepository;
  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedDelay = 60000L)
  public void updateCurrencies() {

    CryptoCurrency BTC = updateInfoFromServer(actualInfoBtc);
    CryptoCurrency ETH = updateInfoFromServer(actualInfoEth);
    CryptoCurrency SOL = updateInfoFromServer(actualInfoSol);

    List<Client> clients = clientRepository.findAll();
    for (Client clientInfo : clients) {
      loggingInfo(BTC, clientInfo);
      loggingInfo(ETH, clientInfo);
      loggingInfo(SOL, clientInfo);
    }
  }

  private void loggingInfo(CryptoCurrency cryptoCurrency, Client clientInfo) {
    if (clientInfo.getSymbol().equals(cryptoCurrency.getSymbol())) {
      BigDecimal changePrice = changePrice(clientInfo.getPrice(), cryptoCurrency.getPriceUsd());
      if (changePrice.abs().doubleValue() > MAX_CHANGE.doubleValue()) {
        log.warn(String.format("Currency: %s, username: %s, change of price: %.3f percent", cryptoCurrency.getSymbol(), clientInfo.getUsername(), changePrice));
      }
    }
  }

  private CryptoCurrency updateInfoFromServer(String url) {
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
      return objectMapper.readValue(response, new TypeReference<List<CryptResponse>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private BigDecimal changePrice(BigDecimal oldPrice, BigDecimal actualPrice) {
    return (actualPrice.subtract(oldPrice).divide(oldPrice, 5, RoundingMode.CEILING)).multiply(CONVERT_TO_PERCENT);
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

package com.begliak.cryptocurrency.service;

import static java.math.RoundingMode.CEILING;

import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.ClientRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

  private static final BigDecimal MAX_CHANGE = BigDecimal.valueOf(1);
  private static final BigDecimal CONVERT_TO_PERCENT = BigDecimal.valueOf(100);

  private final ClientRepository clientRepository;
  private final CryptoCurrencyExternalService currencyExternalService;

  @Scheduled(cron = "${task.notify.change.price}")
  public void updateCurrencies() {
    currencyExternalService.update().forEach(this::notifyClient);
  }

  private void notifyClient(CryptoCurrency cryptoCurrency) {
    var clients = clientRepository.findAll();
    clients.forEach(client -> loggingInfo(cryptoCurrency, client));
  }

  private void loggingInfo(CryptoCurrency cryptoCurrency, Client clientInfo) {
    if (clientInfo.getSymbol().equals(cryptoCurrency.getSymbol())) {
      BigDecimal changePrice = changePrice(clientInfo.getPrice(), cryptoCurrency.getPriceUsd());
      if (changePrice.abs().doubleValue() > MAX_CHANGE.doubleValue()) {
        log.warn(String.format("Currency: %s, username: %s, change of price: %.3f percent", cryptoCurrency.getSymbol(), clientInfo.getUsername(), changePrice));
      }
    }
  }

  private BigDecimal changePrice(BigDecimal oldPrice, BigDecimal actualPrice) {
    return (actualPrice.subtract(oldPrice).divide(oldPrice, 5, CEILING)).multiply(CONVERT_TO_PERCENT);
  }
}

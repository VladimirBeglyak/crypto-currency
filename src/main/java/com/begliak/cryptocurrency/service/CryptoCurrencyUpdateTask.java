package com.begliak.cryptocurrency.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoCurrencyUpdateTask {

  private final CryptoCurrencyExternalService cryptoCurrencyExternalService;
  private final NotificationSender notificationSender;

  @Scheduled(cron = "${task.notify.change.price}")
  public void updateCurrencies() {
    var cryptoCurrencies = cryptoCurrencyExternalService.update();
    cryptoCurrencies.forEach(notificationSender::send);
  }

  public record CryptoCurrencyEventModel(
      String symbol,
      BigDecimal priceUsd
  ) {

  }
}

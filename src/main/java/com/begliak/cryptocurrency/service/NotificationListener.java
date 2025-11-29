package com.begliak.cryptocurrency.service;

import static java.math.RoundingMode.CEILING;

import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.repository.ClientRepository;
import com.begliak.cryptocurrency.service.CryptoCurrencyUpdateTask.CryptoCurrencyEventModel;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationListener {

  private static final BigDecimal MAX_CHANGE = BigDecimal.valueOf(1);
  private static final BigDecimal CONVERT_TO_PERCENT = BigDecimal.valueOf(100);

  private final ClientRepository clientRepository;

  @RabbitListener(
      queues = "${rabbit.settings.notification-queue}",
      containerFactory = "rabbitListenerContainerFactory"
  )
  private void receive(CryptoCurrencyEventModel cryptoCurrency) {
    var clients = clientRepository.findAllBySymbol(cryptoCurrency.symbol());
    clients.forEach(client -> loggingInfo(cryptoCurrency, client));
  }

  private void loggingInfo(CryptoCurrencyEventModel cryptoCurrency, Client clientInfo) {
    if (!clientInfo.getSymbol().equals(cryptoCurrency.symbol())) {
      return;
    }

    BigDecimal changePrice = changePrice(clientInfo.getPrice(), cryptoCurrency.priceUsd());
    if (changePrice.abs().doubleValue() > MAX_CHANGE.doubleValue()) {
      var message = String.format("Currency: %s, username: %s, change of price: %.3f percent",
          cryptoCurrency.symbol(),
          clientInfo.getUsername(),
          changePrice
      );
      log.warn(message);
    }
  }

  private BigDecimal changePrice(BigDecimal oldPrice, BigDecimal actualPrice) {
    return (actualPrice.subtract(oldPrice).divide(oldPrice, 5, CEILING)).multiply(CONVERT_TO_PERCENT);
  }
}

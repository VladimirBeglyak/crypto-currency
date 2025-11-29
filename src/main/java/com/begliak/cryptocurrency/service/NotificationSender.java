package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.config.RabbitProperties;
import com.begliak.cryptocurrency.service.CryptoCurrencyUpdateTask.CryptoCurrencyEventModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSender {

  private final RabbitTemplate rabbitNotificationTemplate;
  private final RabbitProperties props;

  public void send(CryptoCurrencyEventModel event) {
    rabbitNotificationTemplate.convertAndSend(
        props.notificationExchange(),
        props.notificationQueue(),
        event
    );
  }
}

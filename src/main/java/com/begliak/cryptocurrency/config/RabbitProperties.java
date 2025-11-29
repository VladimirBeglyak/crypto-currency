package com.begliak.cryptocurrency.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rabbit.settings")
public record RabbitProperties(
    String notificationExchange,
    String notificationQueue,
    String notificationDeadLetterExchange,
    String notificationDeadLetterQueue
) {

}

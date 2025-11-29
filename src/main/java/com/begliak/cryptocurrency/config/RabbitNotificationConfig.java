package com.begliak.cryptocurrency.config;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitNotificationConfig {

  private final RabbitProperties rabbitProperties;

  @Bean
  Queue notificationQueue() {
    var arguments = dlqArgs(rabbitProperties.notificationDeadLetterQueue(), rabbitProperties.notificationDeadLetterExchange());
    return new Queue(rabbitProperties.notificationQueue(), true, false, false, arguments);
  }

  @Bean
  DirectExchange notificationExchange() {
    return new DirectExchange(rabbitProperties.notificationExchange());
  }

  @Bean
  Binding notificationBinding() {
    return BindingBuilder.bind(notificationQueue())
        .to(notificationExchange())
        .with(rabbitProperties.notificationQueue());
  }

  @Bean
  DirectExchange notificationDeadLetterExchange() {
    return new DirectExchange(rabbitProperties.notificationDeadLetterExchange());
  }

  @Bean
  Queue notificationDeadLetterQueue() {
    return new Queue(rabbitProperties.notificationDeadLetterQueue());
  }

  @Bean
  Binding notificationDlqBinding() {
    return BindingBuilder.bind(notificationDeadLetterQueue())
        .to(notificationDeadLetterExchange())
        .with(rabbitProperties.notificationDeadLetterQueue());
  }

  @Bean
  MessageConverter messageConverter() {
    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
    var rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter());
    return rabbitTemplate;
  }

  @Bean
  SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
    var factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(messageConverter());
    return factory;
  }

  private Map<String, Object> dlqArgs(String dlqName, String dlqExchange) {
    Map<String, Object> args = new HashMap<>();
    args.put("x-dead-letter-exchange", dlqExchange);
    args.put("x-dead-letter-routing-key", dlqName);
    return args;
  }
}

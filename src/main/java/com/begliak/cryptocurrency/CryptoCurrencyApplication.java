package com.begliak.cryptocurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.begliak.cryptocurrency.config")
public class CryptoCurrencyApplication {

  public static void main(String[] args) {
    SpringApplication.run(CryptoCurrencyApplication.class, args);
  }
}

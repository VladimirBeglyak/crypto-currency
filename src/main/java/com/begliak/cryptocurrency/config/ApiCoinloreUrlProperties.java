package com.begliak.cryptocurrency.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.coinlore")
public record ApiCoinloreUrlProperties(
    String baseUrl,
    String tickerUrl,
    List<String> ids
) {

}

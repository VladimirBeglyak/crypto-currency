package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.ClientRepository;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyActuatorService {

    private static final String ACTUAL_INFO_BTC = "https://api.coinlore.net/api/ticker?id=90";
    private static final String ACTUAL_INFO_ETH = "https://api.coinlore.net/api/ticker?id=80";
    private static final String ACTUAL_INFO_SOL = "https://api.coinlore.net/api/ticker?id=48543";
    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final ClientRepository clientRepository;
    private static final Double MAX_CHANGE = 1.0;
    private static final Double CONVERT_TO_PERCENT = 100.0;

    @Scheduled(fixedDelay = 60000L)
    public void updateDataBase() {

        CryptoCurrency BTC = updateInfoFromServer(ACTUAL_INFO_BTC);
        CryptoCurrency ETH = updateInfoFromServer(ACTUAL_INFO_ETH);
        CryptoCurrency SOL = updateInfoFromServer(ACTUAL_INFO_SOL);

        List<Client> clients = clientRepository.findAll();
        for (Client clientInfo : clients) {
            loggingInfo(BTC, clientInfo);
            loggingInfo(ETH, clientInfo);
            loggingInfo(SOL, clientInfo);
        }
    }

    private void loggingInfo(CryptoCurrency cryptoCurrency, Client clientInfo) {
        if (clientInfo.getSymbol().equals(cryptoCurrency.getSymbol())) {
            Double changePrice = changePrice(clientInfo.getPrice(), cryptoCurrency.getPriceUsd());
            if (abs(changePrice) > MAX_CHANGE) {
                log.warn(String.format("Currency: %s, username: %s, change of price: %.5f percent", cryptoCurrency.getSymbol(), clientInfo.getUsername(), changePrice));
            }
        }
    }

    private CryptoCurrency updateInfoFromServer(String url) {
        ResponseEntity<List> response = new RestTemplate().getForEntity(url, List.class);
        List body = response.getBody();
        Map<String, Object> data = (Map<String, Object>) body.get(0);
        String id = (String) data.get("id");
        String symbol = (String) data.get("symbol");
        String price = (String) data.get("price_usd");

        CryptoCurrency cryptoCurrency = CryptoCurrency.builder()
                .id(Long.valueOf(id))
                .symbol(symbol)
                .priceUsd(Double.valueOf(price))
                .build();
        return cryptoCurrencyRepository.save(cryptoCurrency);
    }

    private Double changePrice(Double oldPrice, Double actualPrice) {
        return (actualPrice - oldPrice) / oldPrice * CONVERT_TO_PERCENT;
    }
}

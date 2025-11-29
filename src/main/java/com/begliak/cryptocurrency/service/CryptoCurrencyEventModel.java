package com.begliak.cryptocurrency.service;

import java.math.BigDecimal;

public record CryptoCurrencyEventModel(
    String symbol,
    BigDecimal priceUsd
) {

}

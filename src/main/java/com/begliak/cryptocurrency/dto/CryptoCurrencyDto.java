package com.begliak.cryptocurrency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CryptoCurrencyDto {

  String id;

  String symbol;

  @JsonProperty("price_usd")
  String priceUsd;
}

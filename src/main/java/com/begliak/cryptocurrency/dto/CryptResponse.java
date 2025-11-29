package com.begliak.cryptocurrency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CryptResponse(
    @JsonProperty("id")
    String id,
    @JsonProperty("symbol")
    String symbol,
    @JsonProperty("name")
    String name,
    @JsonProperty("nameid")
    String nameId,
    @JsonProperty("rank")
    int rank,
    @JsonProperty("price_usd")
    double priceUsd,
    @JsonProperty("percent_change_24h")
    double percentChange24h,
    @JsonProperty("percent_change_1h")
    double percentChange1h,
    @JsonProperty("percent_change_7d")
    double percentChange7d,
    @JsonProperty("market_cap_usd")
    double marketCapitalisationUsd,
    @JsonProperty("volume24")
    double volumeOfCoinsLast24h,
    @JsonProperty("volume24a")
    double volumeOfCoinsTraded,
    @JsonProperty("csupply")
    double circulatingSupply,
    @JsonProperty("tsupply")
    double totalSupply,
    @JsonProperty("msupply")
    double maximumSupply
) {

}

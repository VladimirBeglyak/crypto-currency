package com.begliak.cryptocurrency.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyDto {

        String id;

        String symbol;

        @JsonProperty("price_usd")
        String priceUsd;

}

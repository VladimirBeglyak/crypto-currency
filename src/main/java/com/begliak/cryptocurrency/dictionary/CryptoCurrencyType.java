package com.begliak.cryptocurrency.dictionary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CryptoCurrencyType {
  BTC(90),
  ETH(80),
  SOL(48543);

  private final int externalId;
}

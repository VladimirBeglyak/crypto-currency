package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.config.ApiCoinloreUrlProperties;
import com.begliak.cryptocurrency.service.CryptoCurrencyUpdateTask.CryptoCurrencyEventModel;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CryptoCurrencyExternalService {

  private final CryptoCurrencyUpdater cryptCurrencyUpdater;
  private final List<String> urls;

  public CryptoCurrencyExternalService(
      CryptoCurrencyUpdater cryptCurrencyUpdater,
      ApiCoinloreUrlProperties apiCoinloreUrlProperties
  ) {
    this.cryptCurrencyUpdater = cryptCurrencyUpdater;
    urls = apiCoinloreUrlProperties.ids().stream()
        .map(url -> apiCoinloreUrlProperties.tickerUrl().concat(url))
        .toList();
  }

  public List<CryptoCurrencyEventModel> update() {
    return urls.stream()
        .map(cryptCurrencyUpdater::update)
        .toList();
  }
}

package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.dto.ClientNotifyRequest;
import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.exception.CurrencyNotFoundException;
import com.begliak.cryptocurrency.repository.ClientRepository;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

  private final ClientRepository clientRepository;
  private final CryptoCurrencyRepository cryptoCurrencyRepository;

  public void save(ClientNotifyRequest clientNotifyRequest) {

    CryptoCurrency currency = cryptoCurrencyRepository.findBySymbol(clientNotifyRequest.getSymbol())
        .orElseThrow(() -> new CurrencyNotFoundException(String.format("Currency %s not found", clientNotifyRequest.getSymbol())));

    Optional<Client> clientOpt = clientRepository.findByUsernameAndSymbol(clientNotifyRequest.getUsername(), clientNotifyRequest.getSymbol());

    if (clientOpt.isPresent()) {
      clientOpt.get().setSymbol(currency.getSymbol());
      clientOpt.get().setPrice(currency.getPriceUsd());
    } else {
      Client client = Client.builder()
          .username(clientNotifyRequest.getUsername())
          .price(currency.getPriceUsd())
          .symbol(currency.getSymbol())
          .build();
      clientRepository.save(client);
    }
  }
}

package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.ClientRepository;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    public void save(String username, String symbol){
        Optional<Client> maybeClient = clientRepository.findByUsernameAndSymbol(username, symbol);
        if (!maybeClient.isPresent()){
            CryptoCurrency currency = cryptoCurrencyRepository.findBySymbol(symbol).get();
            Client newUser = Client.builder()
                    .username(username)
                    .price(currency.getPriceUsd())
                    .symbol(currency.getSymbol())
                    .build();
            clientRepository.save(newUser);
        }
    }
}

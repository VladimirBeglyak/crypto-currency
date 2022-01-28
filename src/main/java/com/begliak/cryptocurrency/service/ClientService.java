package com.begliak.cryptocurrency.service;

import com.begliak.cryptocurrency.entity.Client;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.repository.ClientRepository;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    public void save(String username, String symbol){
        Optional<Client> client = clientRepository.findByUsernameAndSymbol(username, symbol);
        CryptoCurrency currency = cryptoCurrencyRepository.findBySymbol(symbol).get();
        if (!client.isPresent()){
            Client newUser = Client.builder()
                    .username(username)
                    .price(currency.getPriceUsd())
                    .symbol(currency.getSymbol())
                    .build();
            clientRepository.save(newUser);
        } else {
            client.get().setSymbol(currency.getSymbol());
            client.get().setPrice(currency.getPriceUsd());
            clientRepository.flush();
        }
    }
}

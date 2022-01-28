package com.begliak.cryptocurrency.service;


import com.begliak.cryptocurrency.dto.CryptoCurrencyDto;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.exception.CurrencyNotFoundException;
import com.begliak.cryptocurrency.mapper.CryptoMapperToDto;
import com.begliak.cryptocurrency.repository.CryptoCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class CryptoCurrencyService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CryptoMapperToDto cryptoMapperToDto;

    public List<CryptoCurrencyDto> getAll() {
        return cryptoCurrencyRepository.findAll().stream()
                .map(cryptoMapperToDto::mapFrom)
                .collect(toList());
    }

    public CryptoCurrency findBySymbol(String symbol) {
        return cryptoCurrencyRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CurrencyNotFoundException(String.format("Currency %s not found", symbol)));
    }
}

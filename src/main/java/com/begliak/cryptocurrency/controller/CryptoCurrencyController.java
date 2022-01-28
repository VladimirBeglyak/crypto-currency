package com.begliak.cryptocurrency.controller;

import com.begliak.cryptocurrency.dto.ClientNotifyRequest;
import com.begliak.cryptocurrency.dto.CryptoCurrencyDto;
import com.begliak.cryptocurrency.entity.CryptoCurrency;
import com.begliak.cryptocurrency.service.ClientService;
import com.begliak.cryptocurrency.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crypto")
public class CryptoCurrencyController {

    private final CryptoCurrencyService cryptoCurrencyService;
    private final ClientService clientService;


    @GetMapping("/all")
    public List<CryptoCurrencyDto> getAll() {
        return cryptoCurrencyService.getAll();
    }

    @GetMapping("/{symbol}")
    public CryptoCurrency getCurrency(@PathVariable String symbol) {
        return cryptoCurrencyService.getCurrency(symbol);
    }

    @PostMapping("/notify")
    public void saveClientInfo(ClientNotifyRequest clientNotifyRequest) {
        clientService.save(clientNotifyRequest);
    }
}

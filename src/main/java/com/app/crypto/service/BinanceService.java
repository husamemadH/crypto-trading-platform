package com.app.crypto.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.app.crypto.model.TickerPrice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BinanceService {

  private final RestClient restClient;

  public TickerPrice getPrice(String symbol) {

    return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/v3/ticker/price")
            .queryParam("symbol", symbol)
            .build())
        .retrieve()
        .body(TickerPrice.class);

  }
}

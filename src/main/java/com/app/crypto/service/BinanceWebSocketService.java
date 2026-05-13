package com.app.crypto.service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import com.app.crypto.config.CryptoConfig;
import com.app.crypto.model.binance.BinanceStreamMessage;
import com.app.crypto.model.binance.BinanceTicker;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class BinanceWebSocketService {

  private final CryptoConfig cryptoConfig;

  private final ObjectMapper objectMapper;

  private String binanceWsUrl = buildStreamUrl();

  private ConcurrentHashMap<String, BigDecimal> latestPrices = new ConcurrentHashMap<>();

  public void connect() {

    ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

    URI uri = URI.create(binanceWsUrl);

    client.execute(uri, session -> session.receive()
        .map(WebSocketMessage::getPayloadAsText)
        .flatMap(this::handleMessage)
        .then())
        .doOnError(e -> System.err.println("Binance WS error " + e.getMessage()))
        .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(3)))
        .subscribe();
  }

  private Mono<Void> handleMessage(String rawJson) {

    BinanceStreamMessage binanceStreamMessage = objectMapper.readValue(rawJson, BinanceStreamMessage.class);

    BinanceTicker ticker = binanceStreamMessage.getData();

    latestPrices.put(ticker.getSymbol(), ticker.getCurrentPrice());

    return Mono.empty();

  }

  public BigDecimal getPrice(String symbol) {

    BigDecimal price = latestPrices.get(symbol);

    if (price == null) {

      throw new RuntimeException("No price available for : " + symbol);
    }

    return price;

  }

  private String buildStreamUrl() {

    String streams = cryptoConfig.getSymbols()
        .stream()
        .map(s -> s.toLowerCase() + "@ticker")
        .collect(Collectors.joining("/"));

    return "wss://stream.binance.com:9443/stream?streams=" + streams;
  }

}

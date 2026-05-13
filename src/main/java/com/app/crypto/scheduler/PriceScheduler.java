
package com.app.crypto.scheduler;

import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.crypto.service.BinanceWebSocketService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

  private final BinanceWebSocketService binanceWebSocketService;
  private final SimpMessagingTemplate messagingTemplate;

  @Scheduled(fixedRate = 1000)
  public void streamPrices() {
    binanceWebSocketService.getAllPrices().forEach((symbol, price) -> {

      messagingTemplate.convertAndSend(
          "/topic/prices/" + symbol,
          (Object) Map.of("symbol", symbol, "price", price));

    });
  }

}


package com.app.crypto.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.app.crypto.dto.request.PriceAlertRequest;
import com.app.crypto.model.PriceAlert;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceAlertService {

  private final List<PriceAlert> alerts = new CopyOnWriteArrayList<>();

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final BinanceWebSocketService binanceWebSocketService;

  public PriceAlert createAlert(PriceAlertRequest priceAlertRequest) {

    BigDecimal currentPrice = binanceWebSocketService.getPrice(priceAlertRequest.getSymbol());

    String direction = priceAlertRequest.getTargetPrice().compareTo(currentPrice) >= 0 ? "ABOVE" : "BELOW";

    PriceAlert alert = PriceAlert.builder()
        .id(UUID.randomUUID().toString())
        .symbol(priceAlertRequest.getSymbol())
        .directionl(direction)
        .triggered(false)
        .createdAt(Instant.now())
        .targetPrice(priceAlertRequest.getTargetPrice())
        .build();

    alerts.add(alert);

    return alert;
  }

  public List<PriceAlert> getAllAlerts() {

    return new ArrayList<PriceAlert>(alerts);
  }

  public void checkAlerts(String symbol, BigDecimal currentPrice) {

    alerts.stream()
        .filter(a -> a.getSymbol().equals(symbol))
        .filter(a -> !a.isTriggered())
        .forEach(alert -> {

          boolean triggered = false;

          if (alert.getDirectionl().equals("ABOVE") && currentPrice.compareTo(alert.getTargetPrice()) > 0) {

            triggered = true;
          }

          if (alert.getDirectionl().equals("BELOW") && currentPrice.compareTo(alert.getTargetPrice()) < 0) {
            triggered = true;
          }

          if (triggered) {

            alert.setTriggered(true);
            alert.setTriggeredAt(Instant.now());

            simpMessagingTemplate.convertAndSend(
                "/topic/alerts",
                (Object) Map.of(
                    "symbol", symbol,
                    "targetPrice", alert.getTargetPrice(),
                    "currentPrice", currentPrice,
                    "triggeredAt", Instant.now()));
          }

        });

  }

}

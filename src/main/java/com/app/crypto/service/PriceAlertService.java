
package com.app.crypto.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.app.crypto.dto.request.PriceAlertRequest;
import com.app.crypto.model.PriceAlert;
import com.app.crypto.repository.PriceAlertRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceAlertService {

  private final PriceAlertRepository priceAlertRepository;

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final BinanceWebSocketService binanceWebSocketService;

  public PriceAlert createAlert(PriceAlertRequest priceAlertRequest) {

    BigDecimal currentPrice = binanceWebSocketService.getPrice(priceAlertRequest.getSymbol());

    String direction = priceAlertRequest.getTargetPrice().compareTo(currentPrice) > 0 ? "ABOVE" : "BELOW";

    PriceAlert alert = PriceAlert.builder()
        .symbol(priceAlertRequest.getSymbol())
        .directionl(direction)
        .triggered(false)
        .createdAt(Instant.now())
        .targetPrice(priceAlertRequest.getTargetPrice())
        .build();

    return priceAlertRepository.save(alert);
  }

  public List<PriceAlert> getAllAlerts() {

    return priceAlertRepository.findAll();
  }

  public void checkAlerts(String symbol, BigDecimal currentPrice) {

    priceAlertRepository.findBySymbolAndTriggeredFalse(symbol)
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
            priceAlertRepository.save(alert);
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

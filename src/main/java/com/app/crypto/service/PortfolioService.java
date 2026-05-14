package com.app.crypto.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.crypto.model.Order;
import com.app.crypto.model.PortfolioEntry;
import com.app.crypto.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioService {

  private final BinanceWebSocketService binanceWebSocketService;
  private final OrderRepository orderRepository;

  public List<PortfolioEntry> getPortfolio() {

    List<String> coins = orderRepository.findDistinctSymbol();

    for (String coin : coins) {
      System.out.println(coin);
    }

    return coins.stream()
        .map(this::buildEntry)
        .filter(entry -> entry.getTotalQuantity().compareTo(BigDecimal.ZERO) > 0)
        .collect(Collectors.toList());

  }

  private PortfolioEntry buildEntry(String symbol) {

    List<Order> orders = orderRepository.findBySymbol(symbol);

    List<Order> buys = orders
        .stream()
        .filter(o -> o.getSide().equals("buy"))
        .collect(Collectors.toList());

    List<Order> sells = orders
        .stream()
        .filter(o -> o.getSide().equals("sell"))
        .collect(Collectors.toList());

    BigDecimal totalCoinsBought = buys
        .stream()
        .map(Order::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    System.out.println(totalCoinsBought.toString());

    BigDecimal totalCoinsSold = sells
        .stream()
        .map(Order::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    System.out.println(totalCoinsSold.toString());

    BigDecimal totalCoinsQuantity = totalCoinsBought.subtract(totalCoinsSold);

    System.out.println(totalCoinsQuantity.toString());

    BigDecimal totalSpent = buys
        .stream()
        .map(o -> o.getQuantity().multiply(o.getPrice()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal averageBuyPrice = totalCoinsBought.compareTo(BigDecimal.ZERO) > 0
        ? totalSpent.divide(totalCoinsBought, 3, RoundingMode.HALF_UP)
        : BigDecimal.ZERO;

    BigDecimal currentPrice = binanceWebSocketService.getPrice(symbol);
    BigDecimal currentValue = totalCoinsQuantity.multiply(currentPrice);
    BigDecimal profitLoss = currentValue.subtract(
        totalCoinsQuantity.multiply(averageBuyPrice));

    return PortfolioEntry.builder()
        .symbol(symbol)
        .totalQuantity(totalCoinsQuantity.setScale(2, RoundingMode.HALF_UP))
        .averageBuyPrice(averageBuyPrice.setScale(2, RoundingMode.HALF_UP))
        .currentPrice(currentPrice.setScale(2, RoundingMode.HALF_UP))
        .currentValue(currentValue.setScale(2, RoundingMode.HALF_UP))
        .profitLoss(profitLoss.setScale(2, RoundingMode.HALF_UP))
        .build();
  }

}

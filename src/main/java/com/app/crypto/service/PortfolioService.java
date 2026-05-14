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

    return coins.stream()
        .map(this::buildEntry)
        .filter(entry -> entry.getTotalQuantity().compareTo(BigDecimal.ZERO) > 0)
        .collect(Collectors.toList());

  }

  private PortfolioEntry buildEntry(String symbol) {

    List<Order> orders = orderRepository.findBySymbol(symbol);

    List<Order> buys = orders
        .stream()
        .filter(o -> o.getSide().equals("BUY"))
        .collect(Collectors.toList());

    List<Order> sells = orders
        .stream()
        .filter(o -> o.getSide().equals("SELL"))
        .collect(Collectors.toList());

    BigDecimal totalCoinsBought = buys
        .stream()
        .map(Order::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCoinsSold = sells
        .stream()
        .map(Order::getQuantity)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCoinsQuantity = totalCoinsBought.subtract(totalCoinsSold);

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
        .totalQuantity(totalCoinsQuantity)
        .averageBuyPrice(averageBuyPrice)
        .currentPrice(currentPrice)
        .currentValue(currentValue)
        .profitLoss(profitLoss)
        .build();

  }

}

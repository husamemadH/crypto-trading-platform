package com.app.crypto.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.crypto.model.Order;
import com.app.crypto.model.OrderRequest;
import com.app.crypto.model.TickerPrice;
import com.app.crypto.service.BinanceService;
import com.app.crypto.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CryptoController {

  private final BinanceService binanceService;
  private final OrderService orderService;

  @GetMapping("/price/{symbol}")
  public ResponseEntity<BigDecimal> getPrice(@PathVariable String symbol) {
    TickerPrice tickerPrice = binanceService.getPrice(symbol);

    return ResponseEntity.ok(new BigDecimal(tickerPrice.getPrice()));

  }

  @PostMapping("/order")
  public ResponseEntity<Order> placeOrder(@Valid @RequestBody OrderRequest request) {

    return ResponseEntity.status(201).body(orderService.placeOrder(request));

  }

  @GetMapping("/order")
  public ResponseEntity<List<Order>> getOrders() {

    return ResponseEntity.ok().body(orderService.getAllOrders());
  }

}

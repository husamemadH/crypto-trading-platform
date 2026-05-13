package com.app.crypto.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Order {

  private String id;
  private String side;
  private String symbol;
  private BigDecimal price;
  private BigDecimal quantity;
  private Instant createdAt;

  public static Order create(String symbol, String side, BigDecimal quantity, BigDecimal price) {

    return Order.builder()
        .id(UUID.randomUUID().toString())
        .symbol(symbol)
        .side(side)
        .quantity(quantity)
        .price(price)
        .createdAt(Instant.now())
        .build();

  }

}

package com.app.crypto.model;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String side;
  private String symbol;
  private BigDecimal price;
  private BigDecimal quantity;
  private Instant createdAt;

  public static Order create(String symbol, String side, BigDecimal quantity, BigDecimal price) {

    return Order.builder()
        .symbol(symbol)
        .side(side)
        .quantity(quantity)
        .price(price)
        .createdAt(Instant.now())
        .build();

  }

}

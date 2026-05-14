package com.app.crypto.model;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PriceAlert {

  private String id;
  private String symbol;
  private BigDecimal targetPrice;
  private String directionl; // ABOVE or BELOW
  private boolean triggered;
  private Instant createdAt;
  private Instant triggeredAt;

}

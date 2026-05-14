package com.app.crypto.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioEntry {

  private String symbol;
  private BigDecimal totalQuantity;
  private BigDecimal currentValue;
  private BigDecimal currentPrice;
  private BigDecimal averageBuyPrice;
  private BigDecimal profitLoss;

}

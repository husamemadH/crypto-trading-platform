
package com.app.crypto.model.binance;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BinanceTicker {

  @JsonProperty("s")
  private String symbol;

  @JsonProperty("c")
  private BigDecimal currentPrice;

  @JsonProperty("o")
  private BigDecimal openPrice;

  @JsonProperty("h")
  private BigDecimal highPrice;

  @JsonProperty("l")
  private BigDecimal lowPrice;

  @JsonProperty("v")
  private BigDecimal volume;

}

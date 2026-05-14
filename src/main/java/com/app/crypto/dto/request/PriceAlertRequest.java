package com.app.crypto.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PriceAlertRequest {

  @NotBlank
  private String symbol;

  @NotNull
  private BigDecimal targetPrice;

}

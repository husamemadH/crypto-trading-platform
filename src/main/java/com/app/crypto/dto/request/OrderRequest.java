package com.app.crypto.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OrderRequest {

  @NotBlank
  private String symbol;

  @NotBlank
  @Pattern(regexp = "(?i)BUY|SELL")
  private String side;

  @NotNull
  @DecimalMin("0.01")
  private BigDecimal quantity;

}

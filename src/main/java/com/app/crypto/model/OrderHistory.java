package com.app.crypto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistory {

  private String symbol;

  private List<Order> orders;

  private Long totalOrders;
}

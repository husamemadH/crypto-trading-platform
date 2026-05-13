
package com.app.crypto.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.app.crypto.model.Order;
import com.app.crypto.model.OrderRequest;
import com.app.crypto.model.TickerPrice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final List<Order> orders = new CopyOnWriteArrayList<>();
  private final BinanceService binanceService;

  public Order placeOrder(OrderRequest request) {

    TickerPrice tickerPrice = binanceService.getPrice(request.getSymbol());

    BigDecimal price = new BigDecimal(tickerPrice.getPrice());

    Order order = Order.create(request.getSymbol(), request.getSide(), request.getQuantity(), price);

    orders.add(order);

    return order;

  }

  public List<Order> getAllOrders() {
    return orders;
  }
}


package com.app.crypto.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.app.crypto.dto.request.OrderRequest;
import com.app.crypto.model.Order;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final List<Order> orders = new CopyOnWriteArrayList<>();

  private final BinanceWebSocketService binanceWebSocketService;

  public Order placeOrder(OrderRequest request) {

    BigDecimal price = binanceWebSocketService.getPrice(request.getSymbol());

    Order order = Order.create(request.getSymbol(), request.getSide(), request.getQuantity(), price);

    orders.add(order);

    return order;

  }

  public List<Order> getAllOrders() {
    return orders;
  }
}

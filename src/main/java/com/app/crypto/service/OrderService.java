
package com.app.crypto.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.crypto.dto.request.OrderRequest;
import com.app.crypto.model.Order;
import com.app.crypto.model.OrderHistory;
import com.app.crypto.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final BinanceWebSocketService binanceWebSocketService;

  public Order placeOrder(OrderRequest request) {

    BigDecimal price = binanceWebSocketService.getPrice(request.getSymbol());

    Order order = Order.create(request.getSymbol(), request.getSide(), request.getQuantity(), price);

    orderRepository.save(order);

    return order;

  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public OrderHistory getHistory(String symbol) {
    List<Order> orders = orderRepository.findBySymbol(symbol);
    Long totalOrders = (long) orders.size();
    
    return OrderHistory.builder()
        .symbol(symbol)
        .orders(orders)
        .totalOrders(totalOrders)
        .build();
  }
}

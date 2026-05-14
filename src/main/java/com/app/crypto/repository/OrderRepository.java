package com.app.crypto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.crypto.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findBySymbol(String symbol);

  @Query("SELECT DISTINCT o.symbol FROM Order o")
  List<String> findDistinctSymbol();
}

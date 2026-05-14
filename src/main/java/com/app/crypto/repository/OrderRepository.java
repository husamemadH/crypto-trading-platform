package com.app.crypto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.crypto.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}

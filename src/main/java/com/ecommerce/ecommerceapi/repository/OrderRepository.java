package com.ecommerce.ecommerceapi.repository;

import com.ecommerce.ecommerceapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    // find all orders by customer email
    List<Order> findByCustomerEmail(String email);

    // find orders by status
    List<Order> findByStatus(Order.OrderStatus status);

    // find orders by customer email ordered by date
    List<Order> findByCustomerEmailOrderByCreatedAtDesc(
        String email);
}

package com.ecommerce.ecommerceapi.controller;

import com.ecommerce.ecommerceapi.dto.OrderRequest;
import com.ecommerce.ecommerceapi.dto.OrderResponse;
import com.ecommerce.ecommerceapi.model.Order;
import com.ecommerce.ecommerceapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderResponse>> getOrdersByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(
            orderService.getOrdersByEmail(email));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(
            orderService.updateOrderStatus(id, status));
    }
}

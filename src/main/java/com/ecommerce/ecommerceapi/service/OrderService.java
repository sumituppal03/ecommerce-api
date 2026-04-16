package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.OrderRequest;
import com.ecommerce.ecommerceapi.dto.OrderResponse;
import com.ecommerce.ecommerceapi.exception.BadRequestException;
import com.ecommerce.ecommerceapi.exception.ResourceNotFoundException;
import com.ecommerce.ecommerceapi.model.Order;
import com.ecommerce.ecommerceapi.model.OrderItem;
import com.ecommerce.ecommerceapi.model.Product;
import com.ecommerce.ecommerceapi.repository.OrderRepository;
import com.ecommerce.ecommerceapi.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderRequest.OrderItemRequest itemRequest
                : request.getItems()) {

            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product", "id",
                            itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new BadRequestException(
                    "Insufficient stock for product: "
                    + product.getName()
                    + ". Available: " + product.getStock()
                    + ", Requested: " + itemRequest.getQuantity());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            product.setStock(
                product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            totalAmount += product.getPrice()
                    * itemRequest.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        Order saved = orderRepository.save(order);
        return convertToResponse(saved);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order", "id", id));
        return convertToResponse(order);
    }

    public List<OrderResponse> getOrdersByEmail(String email) {
        return orderRepository
                .findByCustomerEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(
            Long id, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order", "id", id));

        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new BadRequestException(
                "Cannot update a cancelled order");
        }
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new BadRequestException(
                "Cannot update a delivered order");
        }

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return convertToResponse(updated);
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderResponse.OrderItemResponse> itemResponses =
            order.getOrderItems().stream().map(item -> {
                OrderResponse.OrderItemResponse itemResp =
                    new OrderResponse.OrderItemResponse();
                itemResp.setProductId(item.getProduct().getId());
                itemResp.setProductName(item.getProduct().getName());
                itemResp.setQuantity(item.getQuantity());
                itemResp.setPrice(item.getPrice());
                itemResp.setSubtotal(
                    item.getPrice() * item.getQuantity());
                return itemResp;
            }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}

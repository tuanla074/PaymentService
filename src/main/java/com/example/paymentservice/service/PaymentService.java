package com.example.paymentservice.service;

import com.example.paymentservice.model.Order;
import com.example.paymentservice.repository.OrderRepository;
import com.example.paymentservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> getTotalUnpaidBill(List<Long> orderIds, String authHeader) {
        String jwt = authHeader.substring(7); // Remove "Bearer "
        Long userId = jwtUtil.extractUserId(jwt);

        List<Order> orders = orderRepository.findAllById(orderIds)
                .stream()
                .filter(order -> order.getUserId().equals(userId) && !order.getIsPaid())
                .collect(Collectors.toList());

        int totalUnpaid = orders.stream().mapToInt(Order::getTotalBill).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("totalUnpaid", totalUnpaid);
        response.put("unpaidOrders", orders);

        return response;
    }

    public String markOrdersAsPaid(List<Long> orderIds, String authHeader) {
        String jwt = authHeader.substring(7); // Remove "Bearer "
        Long userId = jwtUtil.extractUserId(jwt);

        List<Order> orders = orderRepository.findAllById(orderIds)
                .stream()
                .filter(order -> order.getUserId().equals(userId) && !order.getIsPaid())
                .collect(Collectors.toList());

        orders.forEach(order -> order.setIsPaid(true));
        orderRepository.saveAll(orders);

        return "Orders marked as paid.";
    }
}

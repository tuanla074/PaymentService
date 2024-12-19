package com.example.paymentservice.controller;

import com.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/get-unpaid")
    public ResponseEntity<Map<String, Object>> getTotalUnpaidBill(
            @RequestBody List<Long> orderIds,
            @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = paymentService.getTotalUnpaidBill(orderIds, authHeader);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mark-paid")
    public ResponseEntity<String> markOrdersAsPaid(
            @RequestBody List<Long> orderIds,
            @RequestHeader("Authorization") String authHeader) {
        String message = paymentService.markOrdersAsPaid(orderIds, authHeader);
        return ResponseEntity.ok(message);
    }
}

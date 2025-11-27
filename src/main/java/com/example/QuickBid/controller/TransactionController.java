package com.example.QuickBid.controller;

import com.example.QuickBid.dto.PaymentRequest;
import com.example.QuickBid.dto.PaymentResponse;
import com.example.QuickBid.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = transactionService.processPayment(paymentRequest);
        return ResponseEntity.ok(response);
    }
}
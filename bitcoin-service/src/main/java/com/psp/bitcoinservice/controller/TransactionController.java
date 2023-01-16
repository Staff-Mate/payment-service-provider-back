package com.psp.bitcoinservice.controller;

import com.psp.bitcoinservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{secret}")
    public ResponseEntity<?> createPaymentRequest(@PathVariable String secret) {
        return transactionService.getAllTransactionForMerchant(secret);
    }
}

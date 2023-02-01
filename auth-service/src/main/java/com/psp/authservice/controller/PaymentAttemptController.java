package com.psp.authservice.controller;

import com.psp.authservice.dto.HistoryFilterDto;
import com.psp.authservice.dto.PaymentAttemptDto;
import com.psp.authservice.service.PaymentAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/payment-attempts")
@Slf4j
public class PaymentAttemptController {

    @Autowired
    private PaymentAttemptService paymentAttemptService;

    @PostMapping("/")
    public ResponseEntity<?> savePaymentAttempt(@RequestBody PaymentAttemptDto paymentAttemptDto) {
        return paymentAttemptService.savePaymentAttempt(paymentAttemptDto);
    }
}

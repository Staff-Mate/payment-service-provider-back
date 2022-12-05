package com.psp.authservice.controller;

import com.psp.authservice.dto.NewPaymentDto;
import com.psp.authservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<?> createPayment(@RequestBody NewPaymentDto newPaymentDto) {
        log.debug("POST request received - /payments/. Payload: {}", newPaymentDto);
        return paymentService.createPayment(newPaymentDto);
    }
}

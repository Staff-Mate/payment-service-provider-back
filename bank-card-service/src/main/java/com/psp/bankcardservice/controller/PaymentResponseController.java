package com.psp.bankcardservice.controller;

import com.psp.bankcardservice.dto.PaymentResponseDto;
import com.psp.bankcardservice.service.PaymentResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentResponseController {

    @Autowired
    private PaymentResponseService paymentResponseService;

    @PostMapping("/")
    public ResponseEntity<?> acceptPaymentResponse(@RequestBody PaymentResponseDto paymentResponseDto){
        return paymentResponseService.acceptPaymentResponse(paymentResponseDto);
    }

}

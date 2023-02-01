package com.psp.qrcodeservice.controller;

import com.psp.qrcodeservice.dto.PaymentResponseDto;
import com.psp.qrcodeservice.service.PaymentResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentResponseController {

    @Autowired
    private PaymentResponseService paymentResponseService;

    @PostMapping("/")
    public ResponseEntity<?> acceptPaymentResponse(@RequestBody PaymentResponseDto paymentResponseDto) {
        log.debug("POST request received - /payments/. Payment id: {}, transaction status: {}",
                paymentResponseDto.getPaymentId(), paymentResponseDto.getTransactionStatus());
        return paymentResponseService.acceptPaymentResponse(paymentResponseDto);
    }

}

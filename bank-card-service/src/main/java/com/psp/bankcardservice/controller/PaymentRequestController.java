package com.psp.bankcardservice.controller;

import com.psp.bankcardservice.dto.ServicePaymentDto;
import com.psp.bankcardservice.service.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment-requests")
public class PaymentRequestController {

    @Autowired
    private PaymentRequestService paymentRequestService;

    @PostMapping("/new-request")
    public ResponseEntity<?> createPaymentRequest(@RequestBody ServicePaymentDto servicePaymentDto) {
        return paymentRequestService.createPaymentRequest(servicePaymentDto);
    }


}

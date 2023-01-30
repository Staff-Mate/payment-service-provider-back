package com.psp.paypalservice.controller;

import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.service.PayPalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment-requests")
@Slf4j
public class PaymentRequestController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/new-payment")
    public ResponseEntity<?> createPaymentRequest(@RequestBody ServicePaymentDto servicePaymentDto) {
        log.debug("POST request received - /payment-requests/new-payment. Credentials id: {}, amount: {}",
                servicePaymentDto.getCredentialsId(), servicePaymentDto.getAmount());
        return payPalService.createPayment(servicePaymentDto);
    }

}

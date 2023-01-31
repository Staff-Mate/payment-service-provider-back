package com.psp.paypalservice.controller;

import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.service.PaymentRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payment-requests")
@Slf4j
public class PaymentRequestController {

    @Autowired
    private PaymentRequestService paymentRequestService;

    @GetMapping("/")
    public ResponseEntity<?> henlo(){
        return new ResponseEntity<>("Henlo", HttpStatus.OK );
    }

    @PostMapping("/new-payment")
    public ResponseEntity<?> createPaymentRequest(@RequestBody ServicePaymentDto servicePaymentDto) {
        log.debug("POST request received - /payment-requests/new-payment. Credentials id: {}, amount: {}",
                servicePaymentDto.getCredentialsId(), servicePaymentDto.getAmount());
        return paymentRequestService.createPayment(servicePaymentDto);
    }

}

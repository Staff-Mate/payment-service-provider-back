package com.psp.authservice.controller;

import com.psp.authservice.dto.PaymentMethodDto;
import com.psp.authservice.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/payment-methods")
@Slf4j
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethod(@PathVariable UUID id) {
        return paymentMethodService.getPaymentMethod(id);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPaymentMethod() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @PostMapping("/")
    public ResponseEntity<?> addPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto) {
        log.debug("POST request received - /payment-methods/. Payment method name: {}", paymentMethodDto.getName());
        return paymentMethodService.addPaymentMethod(paymentMethodDto);
    }

    @PutMapping("/")
    public ResponseEntity<?> updatePaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto) {
        log.debug("PUT request received - /payment-methods/. Payment method id: {}", paymentMethodDto.getId());
        return paymentMethodService.updatePaymentMethod(paymentMethodDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable UUID id) {
        log.debug("DELETE request received - /payment-methods/. Payment method id: {}", id);
        return paymentMethodService.deletePaymentMethod(id);
    }
}

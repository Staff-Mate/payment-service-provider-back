package com.psp.authservice.controller;

import com.psp.authservice.dto.PaymentMethodDto;
import com.psp.authservice.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethod(@PathVariable UUID id){
        return paymentMethodService.getPaymentMethod(id);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPaymentMethod(){
        return paymentMethodService.getAllPaymentMethod();
    }

    @PostMapping("/")
    public ResponseEntity<?> addPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto){
        return paymentMethodService.addPaymentMethod(paymentMethodDto);
    }

    @PutMapping("/")
    public ResponseEntity<?> updatePaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto){
        return paymentMethodService.updatePaymentMethod(paymentMethodDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable UUID id){
        return paymentMethodService.deletePaymentMethod(id);
    }
}

package com.psp.authservice.controller;

import com.psp.authservice.dto.EnabledPaymentMethodDto;
import com.psp.authservice.dto.OwnerDto;
import com.psp.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/payment-method")
    public ResponseEntity<?> getAllPaymentMethodsForCompany(Principal principal) {
        return userService.getAllPaymentMethodsForCompany(principal.getName());
    }

    @GetMapping("/payment-method/{apiKey}")
    public ResponseEntity<?> getAllPaymentMethodsForApiKey(@PathVariable String apiKey) {
        return userService.getAllPaymentMethodsForApiKey(apiKey);
    }

    @PostMapping("/payment-method")
    public ResponseEntity<?> addPaymentMethodForCompany(Principal principal, @RequestBody EnabledPaymentMethodDto enabledPaymentMethodDto) {
        log.debug("POST request received - /users/payment-method. User email: {}", principal.getName());
        return userService.addPaymentMethodForCompany(principal.getName(), enabledPaymentMethodDto);
    }

    @DeleteMapping("/payment-method/{id}")
    public ResponseEntity<?> deletePaymentMethodsForCompany(Principal principal, @PathVariable UUID id) {
        log.debug("DELETE request received - /users/payment-method. User email: {}", principal.getName());
        return userService.deleteEnabledPaymentMethod(principal.getName(), id);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateOwnerName(Principal principal, @RequestBody OwnerDto ownerDto) {
        log.debug("PUT request received - /users/. User email: {}", principal.getName());
        return userService.updateOwnerName(principal.getName(), ownerDto);
    }
}

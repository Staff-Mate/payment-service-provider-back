package com.psp.authservice.controller;

import com.psp.authservice.dto.EnabledPaymentMethodDto;
import com.psp.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/payment-method")
    public ResponseEntity<?> getAllPaymentMethodsForCompany(Principal principal) {
        return userService.getAllPaymentMethodsForCompany(principal.getName());
    }

    @PostMapping("/payment-method")
    public ResponseEntity<?> addPaymentMethodForCompany(Principal principal, @RequestBody EnabledPaymentMethodDto enabledPaymentMethodDto) {
        return userService.addPaymentMethodForCompany(principal.getName(), enabledPaymentMethodDto);
    }

    @DeleteMapping("/payment-method/{id}")
    public ResponseEntity<?> getAllPaymentMethodsForCompany(Principal principal, @PathVariable UUID id) {
        return userService.deleteEnabledPaymentMethod(principal.getName(), id);
    }
}

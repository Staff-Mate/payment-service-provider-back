package com.psp.authservice.controller;

import com.psp.authservice.dto.EnabledPaymentMethodDto;
import com.psp.authservice.dto.UserDto;
import com.psp.authservice.dto.UserFilterDto;
import com.psp.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('read_users')")
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('read_users')")
    @PostMapping("/")
    public ResponseEntity<?> getFilteredUsers(@RequestBody UserFilterDto userFilterDto) {
        return userService.getFilteredUsers(userFilterDto);
    }

    @GetMapping("/payment-method")
    public ResponseEntity<?> getAllPaymentMethodsForCompany(Principal principal) {
        return userService.getAllPaymentMethodsForCompany(principal.getName());
    }

    @GetMapping("/payment-method/{paymentAttemptId}")
    public ResponseEntity<?> getAllPaymentMethodsByPaymentAttemptId(@PathVariable UUID paymentAttemptId) {
        return userService.getAllPaymentMethodsByPaymentAttemptId(paymentAttemptId);
    }

    @PreAuthorize("hasAuthority('enable_payment_method')")
    @PostMapping("/payment-method")
    public ResponseEntity<?> addPaymentMethodForCompany(Principal principal, @RequestBody EnabledPaymentMethodDto enabledPaymentMethodDto) {
        log.debug("POST request received - /users/payment-method. User email: {}", principal.getName());
        return userService.addPaymentMethodForCompany(principal.getName(), enabledPaymentMethodDto);
    }

    @PreAuthorize("hasAuthority('disable_payment_method')")
    @DeleteMapping("/payment-method/{id}")
    public ResponseEntity<?> deletePaymentMethodsForCompany(Principal principal, @PathVariable UUID id) {
        log.debug("DELETE request received - /users/payment-method. User email: {}", principal.getName());
        return userService.deleteEnabledPaymentMethod(principal.getName(), id);
    }

    @PreAuthorize("hasAuthority('update_profile')")
    @PutMapping("/")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UserDto userDto) {
        log.debug("PUT request received - /users/. User email: {}", principal.getName());
        return userService.updateProfile(principal.getName(), userDto);
    }
}

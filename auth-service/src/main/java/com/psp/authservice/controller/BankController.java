package com.psp.authservice.controller;

import com.psp.authservice.security.exception.ResourceConflictException;
import com.psp.authservice.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/bank-service")
@Slf4j
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/")
    public ResponseEntity<?> getAllBanks() {
        return bankService.getAllBanks();
    }
}

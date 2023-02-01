package com.psp.authservice.service;

import com.psp.authservice.model.Bank;
import com.psp.authservice.repository.BankRepository;
import com.psp.authservice.security.exception.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public Bank getBankById(String id) {
        Bank bank = bankRepository.findById(UUID.fromString(id)).orElse(null);
        if(bank == null){
            throw new ResourceConflictException("Bank with id: " + id + " does not exist.");
        }
        return bank;
    }

    public ResponseEntity<?> getAllBanks() {
        return new ResponseEntity<>(bankRepository.findAll(), HttpStatus.OK);
    }
}

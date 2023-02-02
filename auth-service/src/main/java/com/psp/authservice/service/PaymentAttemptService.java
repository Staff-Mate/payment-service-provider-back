package com.psp.authservice.service;

import com.psp.authservice.dto.PaymentAttemptDto;
import com.psp.authservice.model.PaymentAttempt;
import com.psp.authservice.repository.BankRepository;
import com.psp.authservice.repository.PaymentAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentAttemptService {

    @Autowired
    private PaymentAttemptRepository paymentAttemptRepository;
    @Autowired
    private BankRepository bankRepository;

    public PaymentAttempt save(PaymentAttemptDto paymentAttemptDto){
        PaymentAttempt paymentAttempt = new PaymentAttempt(paymentAttemptDto.getApiKey(), paymentAttemptDto.getAmount(), paymentAttemptDto.getBillingCycle());
        return paymentAttemptRepository.save(paymentAttempt);
    }

    public ResponseEntity<?> savePaymentAttempt(PaymentAttemptDto paymentAttemptDto) {
        return new ResponseEntity<>("http://192.168.43.124:4200/payment/" + save(paymentAttemptDto).getId(), HttpStatus.OK);
    }

    public PaymentAttempt getPaymentAttempt(UUID paymentAttemptId) {
        return paymentAttemptRepository.findPaymentAttemptById(paymentAttemptId);
    }
}

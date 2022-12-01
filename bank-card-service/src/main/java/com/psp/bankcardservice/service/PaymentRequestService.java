package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.PaymentRequestDto;
import com.psp.bankcardservice.model.PaymentRequest;
import com.psp.bankcardservice.repository.PaymentRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<?> createPaymentRequest(PaymentRequestDto paymentRequestDto) {
        PaymentRequest paymentRequest = modelMapper.map(paymentRequestDto, PaymentRequest.class);
        paymentRequestRepository.save(paymentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

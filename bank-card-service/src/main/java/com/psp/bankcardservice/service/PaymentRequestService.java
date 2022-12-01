package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.ServicePaymentDto;
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

    public ResponseEntity<?> createPaymentRequest(ServicePaymentDto servicePaymentDto) {
        PaymentRequest paymentRequest = modelMapper.map(servicePaymentDto, PaymentRequest.class);
        paymentRequest.setMerchantId(servicePaymentDto.getCredentialsId());
        paymentRequest.setMerchantPassword(servicePaymentDto.getCredentialsSecret());
        paymentRequest.setMerchantTimestamp(servicePaymentDto.getTimestamp());
        paymentRequest.setMerchantOrderId(String.format("%.0f", (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L)));
        paymentRequestRepository.save(paymentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

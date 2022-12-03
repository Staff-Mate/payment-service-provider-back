package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.PaymentRequestDto;
import com.psp.bankcardservice.dto.ServicePaymentDto;
import com.psp.bankcardservice.model.PaymentRequest;
import com.psp.bankcardservice.repository.PaymentRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> createPaymentRequest(ServicePaymentDto servicePaymentDto) {
        PaymentRequest paymentRequest = modelMapper.map(servicePaymentDto, PaymentRequest.class);
        paymentRequest.setMerchantId(servicePaymentDto.getCredentialsId());
        paymentRequest.setMerchantPassword(servicePaymentDto.getCredentialsSecret());
        paymentRequest.setMerchantTimestamp(servicePaymentDto.getTimestamp());
        paymentRequest.setMerchantOrderId(String.format("%.0f", (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L)));
        paymentRequestRepository.save(paymentRequest);
        PaymentRequestDto paymentRequestDto = modelMapper.map(paymentRequest, PaymentRequestDto.class);
        return restTemplate.postForEntity("http://localhost:8080/payments/" , paymentRequestDto, String.class);
    }
}

package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.PaymentRequestDto;
import com.psp.bankcardservice.dto.ServicePaymentDto;
import com.psp.bankcardservice.model.PaymentRequest;
import com.psp.bankcardservice.repository.PaymentRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
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
        paymentRequest.setMerchantTimestamp(servicePaymentDto.getTimestamp());
        paymentRequest.setMerchantOrderId(String.format("%.0f", (Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L)));

        paymentRequestRepository.save(paymentRequest);
        log.debug("Payment request with id {} created. Merchant id : {}, merchant Order id: {}",
                paymentRequest.getId(), paymentRequest.getMerchantId(), paymentRequest.getMerchantOrderId());

        PaymentRequestDto paymentRequestDto = modelMapper.map(paymentRequest, PaymentRequestDto.class);
        paymentRequestDto.setMerchantPassword(servicePaymentDto.getCredentialsSecret());
        ResponseEntity<String> response =  restTemplate.postForEntity( servicePaymentDto.getMerchantBankUrl() +"/payments/" , paymentRequestDto, String.class);
        return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
    }
}

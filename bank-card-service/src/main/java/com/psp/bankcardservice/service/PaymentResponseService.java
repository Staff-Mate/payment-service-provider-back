package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.PaymentResponseDto;
import com.psp.bankcardservice.model.PaymentResponse;
import com.psp.bankcardservice.model.TransactionStatus;
import com.psp.bankcardservice.repository.PaymentResponseRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentResponseService {

    @Autowired
    private PaymentResponseRepository paymentResponseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void save(PaymentResponse paymentResponse) {
        paymentResponseRepository.save(paymentResponse);
    }

    public ResponseEntity<?> acceptPaymentResponse(PaymentResponseDto paymentResponseDto) {
        PaymentResponse paymentResponse = modelMapper.map(paymentResponseDto, PaymentResponse.class);
        paymentResponse.setTransactionStatus(TransactionStatus.valueOf(paymentResponseDto.getTransactionStatus()));

        save(paymentResponse);
        log.debug("Payment response with id {} created. Payment id: {}, transaction status: {}",
                paymentResponse.getId(), paymentResponse.getPaymentId(), paymentResponse.getTransactionStatus());

        return new ResponseEntity<>(paymentResponseDto.getUrl(), HttpStatus.OK);
    }
}
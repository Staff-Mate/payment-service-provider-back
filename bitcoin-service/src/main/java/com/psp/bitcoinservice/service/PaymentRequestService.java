package com.psp.bitcoinservice.service;


import com.psp.bitcoinservice.dto.BtcPaymentResponseDto;
import com.psp.bitcoinservice.dto.ServicePaymentDto;
import com.psp.bitcoinservice.model.BtcPaymentResponse;
import com.psp.bitcoinservice.repository.PaymentRequestRepository;
import com.psp.bitcoinservice.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BtcPaymentResponseService btcPaymentResponseService;

    public ResponseEntity<?> createPaymentRequest(ServicePaymentDto servicePaymentDto) {
        Map<String, Object> createOrderMap = new HashMap<String,Object>();
        createOrderMap.put("order_id", Utils.generateId());
        createOrderMap.put("price_amount", servicePaymentDto.getAmount());
        createOrderMap.put("price_currency","USD");
        createOrderMap.put("receive_currency","USD");
        createOrderMap.put("title", "New order");
        createOrderMap.put("description", "");
        createOrderMap.put("success_url", servicePaymentDto.getSuccessUrl());
        createOrderMap.put("cancel_url", servicePaymentDto.getFailedUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token "+ servicePaymentDto.getCredentialsSecret());
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String,Object>>(createOrderMap, headers);

        ResponseEntity<BtcPaymentResponseDto> response = restTemplate.postForEntity("https://api-sandbox.coingate.com/v2/orders" ,httpEntity, BtcPaymentResponseDto.class);
        BtcPaymentResponseDto btcPaymentResponseDto = response.getBody();
        btcPaymentResponseService.save(new BtcPaymentResponse(btcPaymentResponseDto));
        return new ResponseEntity<>(btcPaymentResponseDto.getPayment_url(),HttpStatus.OK);
    }


}

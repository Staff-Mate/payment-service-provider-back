package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.*;
import com.psp.bankcardservice.model.PaymentRequest;
import com.psp.bankcardservice.repository.PaymentRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
        paymentRequest.setActive(true);

        paymentRequestRepository.save(paymentRequest);
        log.debug("Payment request with id {} created. Merchant id : {}, merchant Order id: {}",
                paymentRequest.getId(), paymentRequest.getMerchantId(), paymentRequest.getMerchantOrderId());

        PaymentRequestDto paymentRequestDto = modelMapper.map(paymentRequest, PaymentRequestDto.class);
        paymentRequestDto.setMerchantPassword(servicePaymentDto.getCredentialsSecret());
        paymentRequestDto.setIsBankCardPayment(true);
        ResponseEntity<String> response =  restTemplate.postForEntity( servicePaymentDto.getMerchantBankUrl() +"/payments/" , paymentRequestDto, String.class);
        return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
    }

    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
        Page<PaymentRequest> requests = paymentRequestRepository.findByMerchantIdAndActive(historyFilterDto.getCredentialsId(), true, pageable);
        Page<HistoryResponseDto> requestsDto = requests.map(paymentRequest -> new HistoryResponseDto(historyFilterDto.getServiceName(), "ACTIVE", paymentRequest.getAmount(), paymentRequest.getMerchantTimestamp()));
        return requestsDto.getContent();
    }

    public void updateActiveStatus(PaymentResponseDto paymentResponseDto) {
        PaymentRequest paymentRequest = paymentRequestRepository.findByMerchantOrderId(paymentResponseDto.getMerchantOrderId());
        paymentRequest.setActive(false);
        paymentRequestRepository.save(paymentRequest);
    }
}

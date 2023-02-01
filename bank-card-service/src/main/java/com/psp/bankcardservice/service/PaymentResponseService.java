package com.psp.bankcardservice.service;

import com.psp.bankcardservice.dto.HistoryFilterDto;
import com.psp.bankcardservice.dto.HistoryResponseDto;
import com.psp.bankcardservice.dto.PaymentResponseDto;
import com.psp.bankcardservice.model.PaymentResponse;
import com.psp.bankcardservice.model.TransactionStatus;
import com.psp.bankcardservice.repository.PaymentResponseRepository;
import com.psp.bankcardservice.repository.specifications.PaymentResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentResponseService {
    @Autowired
    private PaymentRequestService paymentRequestService;

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
        paymentRequestService.updateActiveStatus(paymentResponseDto);

        save(paymentResponse);
        log.debug("Payment response with id {} created. Payment id: {}, transaction status: {}",
                paymentResponse.getId(), paymentResponse.getPaymentId(), paymentResponse.getTransactionStatus());

        return new ResponseEntity<>(paymentResponseDto.getUrl(), HttpStatus.OK);
    }


    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
        Page<PaymentResponse> responses = paymentResponseRepository.findAll(PaymentResponseSpecification.getFilteredResponses(historyFilterDto), pageable);
        Page<HistoryResponseDto> responsesDto = responses.map(paymentResponse -> new HistoryResponseDto(historyFilterDto.getServiceName(), paymentResponse.getTransactionStatus().name(), paymentResponse.getPaymentRequest().getAmount(), paymentResponse.getPaymentRequest().getMerchantTimestamp()));
        return responsesDto.getContent();
    }
}

package com.psp.paypalservice.service;


import com.psp.paypalservice.dto.HistoryFilterDto;
import com.psp.paypalservice.dto.HistoryResponseDto;
import com.psp.paypalservice.model.PayPalPayment;
import com.psp.paypalservice.repository.PaypalPaymentRepository;
import com.psp.paypalservice.repository.specifications.PayPalPaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class HistoryService {

    @Autowired
    private PaypalPaymentRepository paypalPaymentRepository;

    public List<HistoryResponseDto> getAllTransactionForMerchant(HistoryFilterDto historyFilterDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
        Page<PayPalPayment> payments = paypalPaymentRepository.findByMerchantId(historyFilterDto.getCredentialsId(), pageable);
        Page<HistoryResponseDto> responsesDto = payments.map(payment ->
        {
            try {
                return new HistoryResponseDto(historyFilterDto.getServiceName(), payment.getState(),
                        Double.parseDouble(payment.getAmount()), new Timestamp(sdf.parse(payment.getTimestamp()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        });
        return responsesDto.getContent();
    }

    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
        Page<PayPalPayment> payments = paypalPaymentRepository.findAll(
                PayPalPaymentSpecification.getFilteredResponses(historyFilterDto), pageable);
        Page<HistoryResponseDto> responsesDto = payments.map(payment ->
        {
            try {
                return new HistoryResponseDto(historyFilterDto.getServiceName(), payment.getState(),
                        Double.parseDouble(payment.getAmount()), new Timestamp(sdf.parse(payment.getTimestamp()).getTime()));
            } catch (ParseException e) {
                System.out.println("_____________________PROBLEM SA TIMASTAMP)))))))))))))");
                e.printStackTrace();
            }
            return null;
        });

        return responsesDto.getContent();
    }
}


//    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
//        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
//        Page<PaymentRequest> requests = paymentRequestRepository.findByMerchantIdAndActive(historyFilterDto.getCredentialsId(), true, pageable);
//        Page<HistoryResponseDto> requestsDto = requests.map(paymentRequest -> new HistoryResponseDto(historyFilterDto.getServiceName(), "ACTIVE", paymentRequest.getAmount(), paymentRequest.getMerchantTimestamp()));
//        return requestsDto.getContent();
//    }
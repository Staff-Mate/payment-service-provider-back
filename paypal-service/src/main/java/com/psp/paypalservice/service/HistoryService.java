package com.psp.paypalservice.service;


import com.psp.paypalservice.dto.HistoryFilterDto;
import com.psp.paypalservice.dto.HistoryResponseDto;
import com.psp.paypalservice.model.PayPalPayment;
import com.psp.paypalservice.repository.PaypalPaymentRepository;
import com.psp.paypalservice.repository.specifications.PayPalPaymentSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
public class HistoryService {

    @Autowired
    private PaypalPaymentRepository paypalPaymentRepository;

    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
        Page<PayPalPayment> payments = paypalPaymentRepository.findAll(
                PayPalPaymentSpecification.getFilteredResponses(historyFilterDto), pageable);
        log.debug("Payments that fit history filter with status {} and other criteria found. Total elements: {}",
                historyFilterDto.getStatus(), payments.getTotalElements());

        Page<HistoryResponseDto> responsesDto = payments.map(payment ->
        {
            try {
                return new HistoryResponseDto(historyFilterDto.getServiceName(), payment.getState(),
                        Double.parseDouble(payment.getAmount()), new Timestamp(sdf.parse(payment.getTimestamp()).getTime()));
            } catch (ParseException e) {
                System.out.println("_____________________");
                e.printStackTrace();
            }
            return null;
        });

        return responsesDto.getContent();
    }
}
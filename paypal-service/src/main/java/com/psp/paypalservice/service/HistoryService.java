package com.psp.paypalservice.service;


import com.psp.paypalservice.dto.HistoryFilterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;

@Service
public class HistoryService {

    public ResponseEntity<?> getAllTransactionForMerchant(HistoryFilterDto historyFilterDto) throws ParseException {

//        private String serviceName;
//        private String credentialsId;
//        private String credentialsSecret;
//        private String status;
//        private Timestamp startDate;
//        private Timestamp endDate;
//        private int page;
//        private int pageSize;

        //fetch iz payments
        //prepakovati u HistoryResponseDto
//        return new ResponseEntity<>(transactionDtos,HttpStatus.OK);
        throw new ParseException("ee", 1);
    }

    public ResponseEntity<?> getActiveTransactionForMerchant(HistoryFilterDto historyFilterDto) throws ParseException {
        throw new ParseException("ee", 1);
    }
}


//    public List<HistoryResponseDto> getFilteredHistory(HistoryFilterDto historyFilterDto) {
//        Pageable pageable = PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize());
//        Page<PaymentRequest> requests = paymentRequestRepository.findByMerchantIdAndActive(historyFilterDto.getCredentialsId(), true, pageable);
//        Page<HistoryResponseDto> requestsDto = requests.map(paymentRequest -> new HistoryResponseDto(historyFilterDto.getServiceName(), "ACTIVE", paymentRequest.getAmount(), paymentRequest.getMerchantTimestamp()));
//        return requestsDto.getContent();
//    }
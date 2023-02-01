package com.psp.paypalservice.service;


import com.psp.paypalservice.dto.HistoryFilterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class HistoryService {

    public ResponseEntity<?> getAllTransactionForMerchant(HistoryFilterDto historyFilterDto) throws ParseException {
        throw new ParseException("ee", 1);
    }

    public ResponseEntity<?> getActiveTransactionForMerchant(HistoryFilterDto historyFilterDto) throws ParseException {
        throw new ParseException("ee", 1);
    }
}

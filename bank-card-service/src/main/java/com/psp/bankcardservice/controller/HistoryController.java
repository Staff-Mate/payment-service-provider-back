package com.psp.bankcardservice.controller;

import com.psp.bankcardservice.dto.HistoryFilterDto;
import com.psp.bankcardservice.dto.HistoryResponseDto;
import com.psp.bankcardservice.service.PaymentRequestService;
import com.psp.bankcardservice.service.PaymentResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/history")
@Slf4j
public class HistoryController {

    @Autowired
    private PaymentRequestService paymentRequestService;

    @Autowired
    private PaymentResponseService paymentResponseService;


    @GetMapping("/")
    public ResponseEntity<?> getFilteredResponses(@RequestBody HistoryFilterDto historyFilterDto) {
        List<HistoryResponseDto> finishedResponses = paymentResponseService.getFilteredHistory(historyFilterDto);
        return new ResponseEntity<>(finishedResponses, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getFilteredRequests(@RequestBody HistoryFilterDto historyFilterDto) {
        List<HistoryResponseDto> finishedResponses = paymentRequestService.getFilteredHistory(historyFilterDto);
        return new ResponseEntity<>(finishedResponses, HttpStatus.OK);
    }
}

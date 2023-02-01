package com.psp.paypalservice.controller;


import com.psp.paypalservice.dto.HistoryFilterDto;
import com.psp.paypalservice.dto.HistoryResponseDto;
import com.psp.paypalservice.service.HistoryService;
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
    private HistoryService historyService;

    @GetMapping("/")
    public ResponseEntity<?> getAllTransactionForMerchant(@RequestBody HistoryFilterDto historyFilterDto){
        log.debug("GET request received - /history/. Credentials id: {}, status: {}",
                historyFilterDto.getCredentialsId(), historyFilterDto.getStatus());

        List<HistoryResponseDto> responses = historyService.getFilteredHistory(historyFilterDto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveTransactionForMerchant(@RequestBody HistoryFilterDto historyFilterDto){
        log.debug("GET request received - /history/active. Credentials id: {}, status: {}",
                historyFilterDto.getCredentialsId(), historyFilterDto.getStatus());
        List<HistoryResponseDto> responses = historyService.getFilteredHistory(historyFilterDto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}

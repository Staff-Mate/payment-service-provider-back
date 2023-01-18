package com.psp.bitcoinservice.controller;

import com.psp.bitcoinservice.dto.HistoryFilterDto;
import com.psp.bitcoinservice.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/history")
@Slf4j
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/")
    public ResponseEntity<?> getAllTransactionForMerchant(@RequestBody HistoryFilterDto historyFilterDto){
        try{
            return historyService.getAllTransactionForMerchant(historyFilterDto);
        }catch (ParseException e){
            log.error(e.getMessage());
            return  null;
        }
    }
    @GetMapping("/active")
    public ResponseEntity<?> getActiveTransactionForMerchant(@RequestBody HistoryFilterDto historyFilterDto){
        try{
            return historyService.getActiveTransactionForMerchant(historyFilterDto);
        }catch (ParseException e){
            log.error(e.getMessage());
            return  null;
        }
    }
}

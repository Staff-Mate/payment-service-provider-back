package com.psp.authservice.controller;

import com.psp.authservice.dto.HistoryFilterDto;
import com.psp.authservice.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/history")
@Slf4j
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PreAuthorize("hasAuthority('read_transactions')")
    @PostMapping("/")
    public ResponseEntity<?> getFilteredResponses(Principal principal, @RequestBody HistoryFilterDto historyFilterDto) {
        return historyService.getFilteredHistory(principal.getName(), historyFilterDto);
    }

    @PreAuthorize("hasAuthority('read_transactions')")
    @PostMapping("/active")
    public ResponseEntity<?> getFilteredRequests(Principal principal, @RequestBody HistoryFilterDto historyFilterDto) {
        return historyService.getFilteredHistory(principal.getName(), historyFilterDto);
    }
}

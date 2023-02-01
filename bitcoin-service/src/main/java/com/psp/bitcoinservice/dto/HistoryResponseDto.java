package com.psp.bitcoinservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponseDto {
    private String serviceName;
    private String status;
    private Double amount;
    private Timestamp timestamp;

}

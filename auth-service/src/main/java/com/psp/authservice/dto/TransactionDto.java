package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {
    private String serviceName;
    private String status;
    private Double amount;
    private Timestamp timestamp;
}

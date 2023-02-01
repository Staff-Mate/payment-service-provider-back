package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ServicePaymentDto {

    private String credentialsId;

    private String credentialsSecret;

    private Double amount;

    private Timestamp timestamp;

    private String billingCycle; //ONE_TIME, MONTH, YEAR

    private String successUrl;

    private String failedUrl;

    private String errorUrl;

    private String merchantBankUrl;

    public ServicePaymentDto(String credentialsId, String credentialsSecret, Double amount) {
        this.credentialsId = credentialsId;
        this.credentialsSecret = credentialsSecret;
        this.amount = amount;
    }
}

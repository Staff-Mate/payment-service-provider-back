package com.psp.paypalservice.dto;

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

    private String billingCycle;

    private Timestamp timestamp;

    private String successUrl;

    private String failedUrl;

    private String errorUrl;
}
//private String billing;

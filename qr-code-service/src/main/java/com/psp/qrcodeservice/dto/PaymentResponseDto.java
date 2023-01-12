package com.psp.qrcodeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private String paymentId;
    private String url;
    private String transactionStatus;
    private String merchantId;
    private String acquirerOrderId;
    private Timestamp acquirerTimestamp;

}

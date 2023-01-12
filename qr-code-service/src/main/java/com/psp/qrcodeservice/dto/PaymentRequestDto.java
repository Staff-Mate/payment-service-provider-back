package com.psp.qrcodeservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDto {

    private UUID id;

    private String merchantId;

    private String merchantPassword;

    private Double amount;

    private String merchantOrderId;

    private Timestamp merchantTimestamp;

    private String successUrl;

    private String failedUrl;

    private String errorUrl;

}

package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EnabledPaymentMethodDto {

    private UUID id;
    private String userId;
    private String userSecret;
    private PaymentMethodDto paymentMethod;
    private Timestamp dateAdded;
}

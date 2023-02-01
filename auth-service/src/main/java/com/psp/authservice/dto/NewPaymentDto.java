package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NewPaymentDto {

    private String apiKey;

    private Double amount;

    private UUID paymentMethodId;

    private String billingCycle;

}

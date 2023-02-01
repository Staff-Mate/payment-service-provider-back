package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentAttemptDto {

    private String id;

    private String apiKey;

    private Double amount;

    private String billingCycle;
}

package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NewPaymentDto {

    private UUID paymentAttemptId;

    private UUID paymentMethodId;

}

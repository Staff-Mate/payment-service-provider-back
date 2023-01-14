package com.psp.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BtcPaymentResponseDto {
    private Long id;

    private String price_currency;

    private String price_amount;

    private String created_at;

    private String order_id;

    private String payment_url;
}

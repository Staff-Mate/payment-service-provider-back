package com.psp.bitcoinservice.model;

import com.psp.bitcoinservice.dto.BtcPaymentResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BtcPaymentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    private Long btcId;
    @Column
    private String priceCurrency;
    @Column
    private String priceAmount;
    @Column
    private String createdAt;
    @Column
    private String orderId;

    public BtcPaymentResponse(BtcPaymentResponseDto btcPaymentResponseDto) {
        this.btcId=btcPaymentResponseDto.getId();
        this.priceCurrency=btcPaymentResponseDto.getPrice_currency();
        this.priceAmount = btcPaymentResponseDto.getPrice_amount();
        this.createdAt = btcPaymentResponseDto.getCreated_at();
        this.orderId = btcPaymentResponseDto.getOrder_id();
    }
}


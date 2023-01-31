package com.psp.paypalservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PayPalPaymentResponse {
    // TODO

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    //    @Column
    //    private Long btcId;
    //    @Column
    //    private String priceCurrency;
    //    @Column
    //    private String priceAmount;
    //    @Column
    //    private String createdAt;
    //    @Column
    //    private String orderId;
    //
    //    public BtcPaymentResponse(BtcPaymentResponseDto btcPaymentResponseDto) {
    //        this.btcId=btcPaymentResponseDto.getId();
    //        this.priceCurrency=btcPaymentResponseDto.getPrice_currency();
    //        this.priceAmount = btcPaymentResponseDto.getPrice_amount();
    //        this.createdAt = btcPaymentResponseDto.getCreated_at();
    //        this.orderId = btcPaymentResponseDto.getOrder_id();
    //    }

}



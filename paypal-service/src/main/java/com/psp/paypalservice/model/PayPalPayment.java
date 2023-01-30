package com.psp.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPalPayment {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="paymentId")
    private String paymentId;

    @Column(name="payerId")
    private String payerId;

    @Column(name="total")
    private String total;

    @Column(name="currency")
    private String currency;

    @Column(name="timestamp")
    private Long timestamp;

}

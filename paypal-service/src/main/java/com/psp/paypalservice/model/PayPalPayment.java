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

    @Column(name="merchantId")
    private String merchantId;

    @Column(name="successUrl")
    private String successUrl;

    @Column(name="errorUrl")
    private String errorUrl;

    @Column(name="failedUrl")
    private String failedUrl;

    @Column(name="payerId") //posle dobijamo
    private String payerId;

    @Column(name="state")
    private String state;

    @Column(name="amount")
    private String amount;

    @Column(name="timestamp")
    private Long timestamp;

    @Column(name="type")
    private String type; //onetime ili setupess
}

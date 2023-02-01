package com.psp.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPalPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

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

    @Column(name="payerId")
    private String payerId;

    @Column(name="state")
    private String state;

    @Column(name="amount")
    private String amount;

    @Column(name="timestamp")
    private Timestamp timestamp;

    @Column(name="type")
    private String type; //onetime ili setup
}

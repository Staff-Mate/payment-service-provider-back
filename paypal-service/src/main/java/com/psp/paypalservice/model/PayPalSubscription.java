package com.psp.paypalservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PayPalSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name="merchantId")
    private String merchantId;

    @Column(name="successUrl")
    private String successUrl;

    @Column(name="errorUrl")
    private String errorUrl;

    @Column(name="failedUrl")
    private String failedUrl;

    @Column(name="planId")
    private String planId;

    @Column(name="agreementToken")
    private String agreementToken;

    @Column(name="agreementStartDate")
    private String agreementStartDate;

    @Column(name="agreementName")
    private String agreementName;

    @Column(name="agreementState")
    private String agreementState;

    @Column(name="agreementId")
    private String agreementId;

    @Column(name="payerId")
    private String payerId;

    @Column(name="cycles")
    private String cycles;

    @Column(name="billing")
    private String billing;

    @Column(name="setupFee")
    private Double setupFee;

}

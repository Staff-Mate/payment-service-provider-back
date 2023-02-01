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
    private String agreementState; //STVOREN, *kad ima linkove i nije null,
    //AKTIVAN kad se execute, TO PREKO TOKENA STAVIMO

    @Column(name="agreementId")
    private String agreementId; //KAD SE EXECUTEUJE

    @Column(name="payerId")
    private String payerId;

    @Column(name="cycles")
    private Integer cycles;  //iz payment definition

    @Column(name="billing")
    private String billing; //iz payment definition

    @Column(name="setupFee")
    private Double setupFee;

}

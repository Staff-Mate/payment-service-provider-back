package com.psp.paypalservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PaymentRequest {

    // TODO
}

//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//public class PaymentRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @Column(length = 30)
//    private String credentialsId;
//
//    @Column
//    private Double amount;
//
//    @Column(length = 10)
//    private String orderId;
//
//    @CreationTimestamp
//    private Timestamp timestamp;
//
//    @Column
//    private String successUrl;
//
//    @Column
//    private String failedUrl;
//
//    @Column
//    private String errorUrl;
//
//}
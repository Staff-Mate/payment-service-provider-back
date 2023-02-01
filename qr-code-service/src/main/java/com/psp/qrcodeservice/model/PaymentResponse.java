package com.psp.qrcodeservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String paymentId;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(length = 30)
    private String merchantId;

    @Column
    private String acquirerOrderId;

    @Column
    private Timestamp acquirerTimestamp;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "payment_request_id")
    private PaymentRequest paymentRequest;
}

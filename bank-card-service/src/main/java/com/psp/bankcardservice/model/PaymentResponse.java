package com.psp.bankcardservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(length = 10)
    private String paymentId;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(length = 30)
    private String merchantId;

    @Column(length = 10)
    private String acquirerOrderId;

    @Column
    private Timestamp acquirerTimestamp;
}

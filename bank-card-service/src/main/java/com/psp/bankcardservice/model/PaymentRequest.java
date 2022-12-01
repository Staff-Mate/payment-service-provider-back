package com.psp.bankcardservice.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(length = 30)
    private String merchantId;

    @Column(length = 100)
    private String merchantPassword;

    @Column
    private Double amount;

    @Column(length = 10)
    private String merchantOrderId;

    @CreationTimestamp
    private Timestamp merchantTimestamp;

    @Column
    private String successUrl;

    @Column
    private String failedUrl;

    @Column
    private String errorUrl;

}

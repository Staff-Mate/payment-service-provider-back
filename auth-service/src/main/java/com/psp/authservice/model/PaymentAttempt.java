package com.psp.authservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column
    private String apiKey;

    @Column
    private Double amount;
    @Column
    private String billingCycle;

    public PaymentAttempt(String apiKey, Double amount, String billingCycle) {
        this.apiKey = apiKey;
        this.amount = amount;
        this.billingCycle = billingCycle;
    }
}

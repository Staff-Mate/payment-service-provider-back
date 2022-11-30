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
public class EnabledPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column
    private String userId;

    @Column
    private String userSecret;

    @ManyToOne(targetEntity = PaymentMethod.class, fetch = FetchType.EAGER)
    private PaymentMethod paymentMethod;

}

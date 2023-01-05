package com.psp.authservice.model;

import com.psp.authservice.util.AttributeEncryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EnabledPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column
    private String userId;

    @Column
    @Convert(converter = AttributeEncryptor.class)
    private String userSecret;

    @ManyToOne(targetEntity = PaymentMethod.class, fetch = FetchType.EAGER)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    private Timestamp dateAdded;

}

package com.psp.qrcodeservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 10)
    private String paymentId;

    @Column
    private String fileName;

    public QrCode(String paymentId, String fileName) {
        this.paymentId = paymentId;
        this.fileName = fileName;
    }
}

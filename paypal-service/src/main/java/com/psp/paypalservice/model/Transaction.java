package com.psp.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Transaction {
    private String paymentId;
    private String payerId;
    private String agencyId; //webshop- staffmate
}

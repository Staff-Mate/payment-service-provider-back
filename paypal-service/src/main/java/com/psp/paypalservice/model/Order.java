package com.psp.paypalservice.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Order {

    private double total;
    private String currency;
    private String intent; //CAPTURE
    private String description;
    private String method;
    private String clientId;
    private String clientSecret;
    private String agencyId;

}

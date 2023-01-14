package com.psp.authservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RegularUser extends User {

    @Column
    private String companyName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String country;

    @Column
    private String state;

    @Column
    private String city;

    @Column
    private String apiKey;

    @OneToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column
    private String successUrl;

    @Column
    private String failedUrl;

    @Column
    private String errorUrl;

    @OneToMany(targetEntity = EnabledPaymentMethod.class, fetch = FetchType.EAGER)
    private List<EnabledPaymentMethod> enabledPaymentMethods = new ArrayList<>();

}

package com.psp.authservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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

    @OneToMany(targetEntity = EnabledPaymentMethod.class, fetch = FetchType.EAGER)
    private List<EnabledPaymentMethod> enabledPaymentMethods = new ArrayList<>();

}

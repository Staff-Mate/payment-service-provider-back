package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RegularUserDto {

    private String email;
    private String companyName;
    private String firstName;
    private String lastName;
    private List<PaymentMethodDto> paymentMethods;
}

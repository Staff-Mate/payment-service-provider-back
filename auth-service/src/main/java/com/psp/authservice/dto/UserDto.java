package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String companyName;
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String city;
    private String errorUrl;
    private String successUrl;
    private String failedUrl;
    private BankDto bank;

}

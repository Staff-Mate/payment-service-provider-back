package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoggedInUserDto {
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String city;
    private String email;
    private String displayName;
    private String errorUrl;
    private String failedUrl;
    private String successUrl;
    private BankDto bank;
    private String apiKey;
    private List<String> permissions;
}

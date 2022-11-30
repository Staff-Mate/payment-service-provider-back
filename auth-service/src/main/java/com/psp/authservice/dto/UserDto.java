package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String companyName;
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String city;

}

package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordDto {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

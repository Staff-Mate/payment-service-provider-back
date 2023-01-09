package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LoggedInUserDto {

    private String email;
    private String displayName;
    private List<String> permissions;
}

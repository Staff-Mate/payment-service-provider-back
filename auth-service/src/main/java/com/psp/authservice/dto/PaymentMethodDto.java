package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDto {

    private UUID id;
    private String name;
    private String imagePath;
    private String serviceName;
    private String description;
    private Boolean requiresCredentialsId;
    private Boolean requiresCredentialsSecret;
}

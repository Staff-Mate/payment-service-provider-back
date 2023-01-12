package com.psp.qrcodeservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentInfoDto {

    private String merchantCompanyName;

    private String merchantAccountNumber;

    private Double amount;

}

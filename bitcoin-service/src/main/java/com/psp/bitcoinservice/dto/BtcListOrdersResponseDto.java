package com.psp.bitcoinservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BtcListOrdersResponseDto {

    List<BtcPaymentResponseDto> orders;
}

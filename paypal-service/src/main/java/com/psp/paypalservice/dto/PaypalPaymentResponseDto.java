package com.psp.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPaymentResponseDto {
    //https://example.com/return?paymentId=PAYID-MPMOTVA82587878KX934172W&token=EC-64689213Y9451631J&PayerID=8QV4E6BDM47D6
    //TODO pogledati iz postmana
    private String paymentId;
    private String token;
    private String PayerID;

}
//
////LIST ORDERS DTO MILSLIM D ANE TREBA
//// private Long id;
////
////    private String price_currency;
////
////    private String price_amount;
////
////    private String created_at;
////
////    private String order_id;
////
////    private String payment_url;
////
////    private String status;
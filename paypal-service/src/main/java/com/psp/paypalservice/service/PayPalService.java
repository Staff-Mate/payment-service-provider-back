package com.psp.paypalservice.service;

import com.psp.paypalservice.dto.ServicePaymentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class PayPalService {


    public ResponseEntity<?> createPayment(ServicePaymentDto servicePaymentDto) {

        //da li je payment enabled za tu firmu proverava auth service

        //da li je requestID vec procesiran? Mi nemamo to



        return null;
    }
}

//    private double price;
//    private String currency;
//    private String method;
//    private String intent;
//    private String description;
//    private String clientId;
//    private String clientSecret;
//    private String webShopId;


//    private String credentialsId;
//    private String credentialsSecret;
//    private Double amount;
//    private Timestamp timestamp;
//    private String successUrl;
//    private String failedUrl;
//    private String errorUrl;
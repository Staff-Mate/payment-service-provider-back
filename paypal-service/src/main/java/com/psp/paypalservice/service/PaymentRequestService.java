package com.psp.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.repository.PaymentRequestRepository;
import com.psp.paypalservice.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PaymentRequestService {

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

//    private APIContext apiContext;

    // 1 CREATE ORDER

    // 2 WAIT FOR CUSTOMER TO APPROVE ORDER

    // 3 kad je prihvaceno, CAPTURE the payment, log details

    // TREBA kao 2 endpointa na beku- orders - one to create orders i oder capture- one to capture payments

    public ResponseEntity<?> createPayment(ServicePaymentDto servicePaymentDto) {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls urls = new RedirectUrls();
        urls.setCancelUrl(servicePaymentDto.getFailedUrl()); // ili error url?
        urls.setReturnUrl(servicePaymentDto.getSuccessUrl());

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(Double.toString(servicePaymentDto.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent("authorize");
        payment.setPayer(payer);
        payment.setRedirectUrls(urls);
        payment.setTransactions(transactions);

        APIContext apiContext = new APIContext(servicePaymentDto.getCredentialsId(),
                servicePaymentDto.getCredentialsSecret(),"sandbox");

        try{
            payment = payment.create(apiContext);

            if(payment.getState().equals("created")) {
                //snimiti
                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        return new ResponseEntity<String>(link.getHref(), HttpStatus.OK);
                    }
                }
            }
        } catch(PayPalRESTException e) {
            //log
            e.printStackTrace();
            return new ResponseEntity<String>(servicePaymentDto.getErrorUrl(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(servicePaymentDto.getFailedUrl(), HttpStatus.BAD_REQUEST);
    }

    //https://example.com/return?paymentId=PAYID-MPMOTVA82587878KX934172W&token=EC-64689213Y9451631J&PayerID=8QV4E6BDM47D6
//    DOBILA SAM  MOJ SUCCESS URL SA PARAMETRIMA PAYMENT ID i TOKEN i PAYERID

}
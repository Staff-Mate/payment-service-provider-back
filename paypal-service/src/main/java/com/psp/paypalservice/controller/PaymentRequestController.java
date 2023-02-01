package com.psp.paypalservice.controller;

import com.paypal.base.rest.PayPalRESTException;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.service.PaymentRequestService;
import com.psp.paypalservice.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payment-requests")
@Slf4j
public class PaymentRequestController {

    @Autowired
    private PaymentRequestService paymentRequestService;

    @PostMapping("/new-payment")
    public ResponseEntity<?> createPaymentRequest(@RequestBody ServicePaymentDto servicePaymentDto) throws PayPalRESTException {
        log.debug("POST request received - /payment-requests/new-payment. Credentials id: {}, amount: {}",
                servicePaymentDto.getCredentialsId(), servicePaymentDto.getAmount());

        if(servicePaymentDto.getBillingCycle().equals("ONE_TIME")){
            return paymentRequestService.createPayment(servicePaymentDto);
        }
        return paymentRequestService.createSubscription(servicePaymentDto);
    }

    @GetMapping("/success")
    public String successPayment(@RequestParam String paymentId, @RequestParam String token, @RequestParam String PayerID) {
        String resultingUrl = paymentRequestService.executePayment(paymentId, token, PayerID);
        Utils.browse(resultingUrl);
        return resultingUrl;
    }

    @GetMapping("/subsuccess")
    public ResponseEntity<?> successSubscription(@RequestParam String token, @RequestParam String ba_token){
        return paymentRequestService.executeAgreement(token, ba_token);
    }

    @GetMapping("/")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>("Test", HttpStatus.OK );
    }

}

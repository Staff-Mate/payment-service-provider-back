package com.psp.paypalservice.controller;

import com.psp.paypalservice.dto.PaypalPaymentResponseDto;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.model.PayPalPaymentResponse;
import com.psp.paypalservice.service.PaymentRequestService;
import lombok.Getter;
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

    @GetMapping("/")
    public ResponseEntity<?> henlo(){
        return new ResponseEntity<>("Henlo", HttpStatus.OK );
    }

    @PostMapping("/new-payment")
    public ResponseEntity<?> createPaymentRequest(@RequestBody ServicePaymentDto servicePaymentDto) {
        log.debug("POST request received - /payment-requests/new-payment. Credentials id: {}, amount: {}",
                servicePaymentDto.getCredentialsId(), servicePaymentDto.getAmount());

        if(servicePaymentDto.getBillingCycle().equals("ONE_TIME")){
            return paymentRequestService.createPayment(servicePaymentDto);
        }

        return paymentRequestService.createSubscription(servicePaymentDto);
    }

    ////https://example.com/return?paymentId=PAYID-MPMOTVA82587878KX934172W&token=EC-64689213Y9451631J&PayerID=8QV4E6BDM47D6
    @GetMapping("/success")
    public ResponseEntity<?> successp(@RequestParam String paymentId, @RequestParam String token, @RequestParam String PayerID) {
//        return paymentRequestService.executePayment(paymentId, token, PayerID);
        System.out.println(" _______________________________________________ PARAMS");
        //Sve je tu
        return paymentRequestService.executePayment(paymentId, token, PayerID);
    }

    @PostMapping("/success") // TODO predlog za body, da se prepakuje na frontu
    public ResponseEntity<?> success(@RequestBody PaypalPaymentResponseDto dto) {
        System.out.println(" _______________________________________________ BODY");
        System.out.print(dto.getPaymentId());
        System.out.print(dto.getPayerID());
        System.out.print(dto.getToken());
        return new ResponseEntity<>("<h1>BODY</h1>", HttpStatus.OK);
    }

}

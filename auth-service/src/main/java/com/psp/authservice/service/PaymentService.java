package com.psp.authservice.service;

import com.psp.authservice.dto.NewPaymentDto;
import com.psp.authservice.dto.ServicePaymentDto;
import com.psp.authservice.model.EnabledPaymentMethod;
import com.psp.authservice.model.RegularUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
@Slf4j
public class PaymentService {

    private final String API_GATEWAY = "http://localhost:9000/";
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> createPayment(NewPaymentDto newPaymentDto) {
        RegularUser user = userService.findUserByApiKey(newPaymentDto.getApiKey());
        EnabledPaymentMethod enabledPaymentMethod = userService.getPaymentMethodForCompany(user, newPaymentDto.getPaymentMethodId());
        if (enabledPaymentMethod != null) {
            log.debug("Payment process started with {}, for merchant: {}", enabledPaymentMethod.getPaymentMethod().getName(), user.getId());
            ServicePaymentDto servicePaymentDto = new ServicePaymentDto(enabledPaymentMethod.getUserId(), enabledPaymentMethod.getUserSecret(), newPaymentDto.getAmount(), newPaymentDto.getBillingCycle());
            servicePaymentDto.setTimestamp(new Timestamp(System.currentTimeMillis()));
            servicePaymentDto.setMerchantBankUrl(user.getBank().getBankUrl());
            servicePaymentDto.setErrorUrl(user.getErrorUrl());
            servicePaymentDto.setFailedUrl(user.getFailedUrl());
            servicePaymentDto.setSuccessUrl(user.getSuccessUrl());
            return restTemplate.postForEntity(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + "/payment-requests/new-payment", servicePaymentDto, String.class);
        } else {
            log.error("Payment attempt with unsupported method: {}, for merchant: {}", newPaymentDto.getPaymentMethodId(), user.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}

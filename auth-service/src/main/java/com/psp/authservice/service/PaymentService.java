package com.psp.authservice.service;

import com.psp.authservice.dto.NewPaymentDto;
import com.psp.authservice.dto.ServicePaymentDto;
import com.psp.authservice.model.EnabledPaymentMethod;
import com.psp.authservice.model.RegularUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
public class PaymentService {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private final String API_GATEWAY = "http://localhost:9000/";

    public ResponseEntity<?> createPayment(NewPaymentDto newPaymentDto) {
        RegularUser user = userService.findUserByApiKey(newPaymentDto.getApiKey());
        EnabledPaymentMethod enabledPaymentMethod = userService.getPaymentMethodForCompany(user, newPaymentDto.getPaymentMethodId());
        if (enabledPaymentMethod != null) {
            ServicePaymentDto servicePaymentDto = new ServicePaymentDto(enabledPaymentMethod.getUserId(), enabledPaymentMethod.getUserSecret(), newPaymentDto.getAmount());
            servicePaymentDto.setTimestamp(new Timestamp(System.currentTimeMillis()));
            //TODO: add urls
            return restTemplate.postForEntity(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + "/new-payment", servicePaymentDto, String.class);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}

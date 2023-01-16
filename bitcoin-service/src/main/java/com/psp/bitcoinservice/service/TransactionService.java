package com.psp.bitcoinservice.service;

import com.psp.bitcoinservice.dto.BtcListOrdersResponseDto;
import com.psp.bitcoinservice.dto.BtcPaymentResponseDto;
import com.psp.bitcoinservice.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BtcPaymentResponseService btcPaymentResponseService;

    public ResponseEntity<?> getAllTransactionForMerchant(String secret) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token "+ secret);
        ResponseEntity<BtcListOrdersResponseDto> response = restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders?sort=created_at_desc", HttpMethod.GET, new HttpEntity<>(headers), BtcListOrdersResponseDto.class);

        BtcListOrdersResponseDto btcListOrdersResponseDto = response.getBody();
        List<TransactionDto> transactionDtos = new ArrayList<>();
        if (btcListOrdersResponseDto != null) {
            for (BtcPaymentResponseDto btcPaymentResponse : btcListOrdersResponseDto.getOrders()) {
                if (btcPaymentResponse.getStatus().equals("paid"))
                    transactionDtos.add(new TransactionDto("SUCCESS",Double.parseDouble(btcPaymentResponse.getPrice_amount()), btcPaymentResponse.getCreated_at()));
                else if ((btcPaymentResponse.getStatus().equals("invalid") || btcPaymentResponse.getStatus().equals("expired")))
                    transactionDtos.add(new TransactionDto("FAILED",Double.parseDouble(btcPaymentResponse.getPrice_amount()), btcPaymentResponse.getCreated_at()));

            }
        }
        return new ResponseEntity<>(transactionDtos,HttpStatus.OK);
    }
}

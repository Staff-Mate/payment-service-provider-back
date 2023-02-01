package com.psp.bitcoinservice.service;

import com.psp.bitcoinservice.dto.*;
import com.psp.bitcoinservice.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BtcPaymentResponseService btcPaymentResponseService;

    public ResponseEntity<?> getAllTransactionForMerchant(HistoryFilterDto historyFilterDto) throws ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token "+ historyFilterDto.getCredentialsSecret());

        Map<String, String> params = new HashMap<>();
        params.put("created_at[from]", historyFilterDto.getStartDate() != null ? historyFilterDto.getStartDate().toString().split(" ")[0] : "");
        params.put("created_at[to]", historyFilterDto.getEndDate() != null ? historyFilterDto.getEndDate().toString().split(" ")[0] : "");
        params.put("sort", "created_at_desc");
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BtcListOrdersResponseDto> response = restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders?sort={sort}&created_at[from]={created_at[from]}&created_at[to]={created_at[to]}",HttpMethod.GET, httpEntity,BtcListOrdersResponseDto.class, params);

        BtcListOrdersResponseDto btcListOrdersResponseDto = response.getBody();
        List<HistoryResponseDto> transactionDtos = new ArrayList<>();
        if (btcListOrdersResponseDto != null) {
            for (BtcPaymentResponseDto btcPaymentResponse : btcListOrdersResponseDto.getOrders()) {
                if (btcPaymentResponse.getStatus().equals("paid"))
                    transactionDtos.add(new HistoryResponseDto(historyFilterDto.getServiceName(), "SUCCESS",Double.parseDouble(btcPaymentResponse.getPrice_amount()), getTimestamp(btcPaymentResponse.getCreated_at())));
                else if ((btcPaymentResponse.getStatus().equals("invalid") || btcPaymentResponse.getStatus().equals("expired")))
                    transactionDtos.add(new HistoryResponseDto(historyFilterDto.getServiceName(),"FAILED",Double.parseDouble(btcPaymentResponse.getPrice_amount()), getTimestamp(btcPaymentResponse.getCreated_at())));

            }
        }

        if(historyFilterDto.getStatus() != null && !historyFilterDto.getStatus().equals("")){
            transactionDtos = transactionDtos.stream().filter(historyResponseDto -> historyResponseDto.getStatus().equals(historyFilterDto.getStatus())).collect(Collectors.toList());
        }

        return new ResponseEntity<>(transactionDtos,HttpStatus.OK);
    }

    private static Timestamp getTimestamp(String createdAt) throws ParseException {
        createdAt = createdAt.split("\\+")[0];
        return new Timestamp(DateUtil.provideDateFormat().parse(createdAt).getTime());
    }

    public ResponseEntity<?> getActiveTransactionForMerchant(HistoryFilterDto historyFilterDto) throws ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token "+ historyFilterDto.getCredentialsSecret());

        Map<String, String> params = new HashMap<>();
        params.put("created_at[from]", historyFilterDto.getStartDate() != null ? historyFilterDto.getStartDate().toString().split(" ")[0] : "");
        params.put("created_at[to]", historyFilterDto.getEndDate() != null ? historyFilterDto.getEndDate().toString().split(" ")[0] : "");
        params.put("sort", "created_at_desc");
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BtcListOrdersResponseDto> response = restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders?sort={sort}&created_at[from]={created_at[from]}&created_at[to]={created_at[to]}",HttpMethod.GET, httpEntity,BtcListOrdersResponseDto.class, params);

        BtcListOrdersResponseDto btcListOrdersResponseDto = response.getBody();
        List<HistoryResponseDto> transactionDtos = new ArrayList<>();
        if (btcListOrdersResponseDto != null) {
            for (BtcPaymentResponseDto btcPaymentResponse : btcListOrdersResponseDto.getOrders()) {
                if (btcPaymentResponse.getStatus().equals("new") || btcPaymentResponse.getStatus().equals("pending") )
                    transactionDtos.add(new HistoryResponseDto(historyFilterDto.getServiceName(), "ACTIVE",Double.parseDouble(btcPaymentResponse.getPrice_amount()), getTimestamp(btcPaymentResponse.getCreated_at())));
            }
        }

        if(historyFilterDto.getStatus() != null && !historyFilterDto.getStatus().equals("")){
            transactionDtos = transactionDtos.stream().filter(historyResponseDto -> historyResponseDto.getStatus().equals(historyFilterDto.getStatus())).collect(Collectors.toList());
        }

        return new ResponseEntity<>(transactionDtos,HttpStatus.OK);
    }
}

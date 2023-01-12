package com.psp.authservice.service;

import com.psp.authservice.dto.HistoryFilterDto;
import com.psp.authservice.dto.ServiceHistoryFilterDto;
import com.psp.authservice.dto.TransactionDto;
import com.psp.authservice.model.RegularUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class HistoryService {

    @Autowired
    private UserService userService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;

    private final String API_GATEWAY = "http://localhost:9000/";

    public ResponseEntity<?> getFilteredResponses(String merchantEmail, HistoryFilterDto historyFilterDto) {
        RegularUser user = (RegularUser) userService.loadUserByUsername(merchantEmail);
        ServiceHistoryFilterDto serviceHistoryFilterDto = modelMapper.map(historyFilterDto, ServiceHistoryFilterDto.class);
        serviceHistoryFilterDto.setPageSize(historyFilterDto.getPageSize()/paymentMethodService.findAllPaymentMethods().size());
        List<TransactionDto> transactions = new ArrayList<>();
        if(historyFilterDto.getServiceId() != null && !historyFilterDto.getServiceId().equals("")){
            user.getEnabledPaymentMethods().forEach(enabledPaymentMethod -> {
                if(enabledPaymentMethod.getPaymentMethod().getId().toString().equals(historyFilterDto.getServiceId())){
                    serviceHistoryFilterDto.setMerchantId(enabledPaymentMethod.getUserId());
                    serviceHistoryFilterDto.setServiceName(enabledPaymentMethod.getPaymentMethod().getServiceName());
                    ResponseEntity<TransactionDto[]> responseEntity = restTemplate.exchange(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + "/history/", HttpMethod.GET,new HttpEntity<>(serviceHistoryFilterDto) , TransactionDto[].class);
                    transactions.addAll(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
                }
            });
        }else{
            user.getEnabledPaymentMethods().forEach(enabledPaymentMethod -> {
                serviceHistoryFilterDto.setMerchantId(enabledPaymentMethod.getUserId());
                serviceHistoryFilterDto.setServiceName(enabledPaymentMethod.getPaymentMethod().getServiceName());
                ResponseEntity<TransactionDto[]> responseEntity = restTemplate.exchange(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + "/history/", HttpMethod.GET,new HttpEntity<>(serviceHistoryFilterDto) , TransactionDto[].class);
                transactions.addAll(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
            });
        }
        final Page<TransactionDto> page = new PageImpl<>(transactions, PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize()), transactions.size());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    public ResponseEntity<?> getFilteredRequests(String merchantEmail, HistoryFilterDto historyFilterDto) {
        RegularUser user = (RegularUser) userService.loadUserByUsername(merchantEmail);
        ServiceHistoryFilterDto serviceHistoryFilterDto = modelMapper.map(historyFilterDto, ServiceHistoryFilterDto.class);
        serviceHistoryFilterDto.setPageSize(historyFilterDto.getPageSize()/paymentMethodService.findAllPaymentMethods().size());
        List<TransactionDto> transactions = new ArrayList<>();
        if(historyFilterDto.getServiceId() != null && !historyFilterDto.getServiceId().equals("")){
            user.getEnabledPaymentMethods().forEach(enabledPaymentMethod -> {
                if(enabledPaymentMethod.getPaymentMethod().getId().toString().equals(historyFilterDto.getServiceId())){
                    serviceHistoryFilterDto.setMerchantId(enabledPaymentMethod.getUserId());
                    serviceHistoryFilterDto.setServiceName(enabledPaymentMethod.getPaymentMethod().getServiceName());
                    ResponseEntity<TransactionDto[]> responseEntity = restTemplate.exchange(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + "/history/active", HttpMethod.GET,new HttpEntity<>(serviceHistoryFilterDto) , TransactionDto[].class);
                    transactions.addAll(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
                }
            });
        }else{
            user.getEnabledPaymentMethods().forEach(enabledPaymentMethod -> {
                serviceHistoryFilterDto.setMerchantId(enabledPaymentMethod.getUserId());
                serviceHistoryFilterDto.setServiceName(enabledPaymentMethod.getPaymentMethod().getServiceName());
                ResponseEntity<TransactionDto[]> responseEntity = restTemplate.exchange(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + "/history/active", HttpMethod.GET,new HttpEntity<>(serviceHistoryFilterDto) , TransactionDto[].class);
                transactions.addAll(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
            });
        }
        final Page<TransactionDto> page = new PageImpl<>(transactions, PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize()), transactions.size());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}

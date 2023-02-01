package com.psp.authservice.service;

import com.psp.authservice.dto.HistoryFilterDto;
import com.psp.authservice.dto.ServiceHistoryFilterDto;
import com.psp.authservice.dto.TransactionDto;
import com.psp.authservice.model.EnabledPaymentMethod;
import com.psp.authservice.model.RegularUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    private final String API_GATEWAY = "http://localhost:9000/";
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;

    private static String findHistoryPath(String status) {
        String path = "/history/";
        if (status != null) {
            if (status.equals("ACTIVE")) {
                path += "active";
            }
        }
        return path;
    }

    public ResponseEntity<?> getFilteredHistory(String merchantEmail, HistoryFilterDto historyFilterDto) {
        String path = findHistoryPath(historyFilterDto.getStatus());
        RegularUser user = (RegularUser) userService.loadUserByUsername(merchantEmail);
        ServiceHistoryFilterDto serviceHistoryFilterDto = modelMapper.map(historyFilterDto, ServiceHistoryFilterDto.class);
        serviceHistoryFilterDto.setPageSize(historyFilterDto.getPageSize() / paymentMethodService.findAllPaymentMethods().size());
        List<TransactionDto> transactions = new ArrayList<>();
        if (historyFilterDto.getServiceId() != null && !historyFilterDto.getServiceId().equals("")) {
            user.getEnabledPaymentMethods().forEach(enabledPaymentMethod -> {
                if (enabledPaymentMethod.getPaymentMethod().getId().toString().equals(historyFilterDto.getServiceId())) {
                    transactions.addAll(getTransactions(path, serviceHistoryFilterDto, enabledPaymentMethod));
                }
            });
        } else {
            user.getEnabledPaymentMethods().forEach(enabledPaymentMethod -> transactions.addAll(getTransactions(path, serviceHistoryFilterDto, enabledPaymentMethod)));
        }
        transactions.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
        final Page<TransactionDto> page = new PageImpl<>(transactions, PageRequest.of(historyFilterDto.getPage(), historyFilterDto.getPageSize()), transactions.size());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    private List<TransactionDto> getTransactions(String path, ServiceHistoryFilterDto serviceHistoryFilterDto, EnabledPaymentMethod enabledPaymentMethod) {
        serviceHistoryFilterDto.setCredentialsId(enabledPaymentMethod.getUserId());
        serviceHistoryFilterDto.setCredentialsSecret(enabledPaymentMethod.getUserSecret());
        serviceHistoryFilterDto.setServiceName(enabledPaymentMethod.getPaymentMethod().getServiceName());
        ResponseEntity<TransactionDto[]> responseEntity = restTemplate.exchange(API_GATEWAY + enabledPaymentMethod.getPaymentMethod().getServiceName() + path, HttpMethod.GET, new HttpEntity<>(serviceHistoryFilterDto), TransactionDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }


}

package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ServiceHistoryFilterDto {
    private String serviceName;
    private String credentialsId;
    private String credentialsSecret;
    private String status;
    private Timestamp startDate;
    private Timestamp endDate;
    private int page;
    private int pageSize;
}

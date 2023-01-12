package com.psp.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class HistoryFilterDto {
    private String status;
    private String serviceId;
    private Timestamp startDate;
    private Timestamp endDate;
    private int page;
    private int pageSize;

}

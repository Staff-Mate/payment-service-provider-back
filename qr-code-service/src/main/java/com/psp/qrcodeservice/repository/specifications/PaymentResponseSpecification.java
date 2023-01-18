package com.psp.qrcodeservice.repository.specifications;


import com.psp.qrcodeservice.dto.HistoryFilterDto;
import com.psp.qrcodeservice.model.PaymentRequest;
import com.psp.qrcodeservice.model.PaymentResponse;
import com.psp.qrcodeservice.model.TransactionStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentResponseSpecification {
    public static Specification<PaymentResponse> getFilteredResponses(HistoryFilterDto historyFilterDto) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<PaymentRequest, PaymentResponse> requestResponseJoin = root.join("paymentRequest");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("merchantId"), historyFilterDto.getCredentialsId()));

            if (historyFilterDto.getStatus() != null && !historyFilterDto.getStatus().equals("")) {
                predicates.add(criteriaBuilder.equal(root.get("transactionStatus"), TransactionStatus.valueOf(historyFilterDto.getStatus())));
            }
            if (historyFilterDto.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThan(requestResponseJoin.get("merchantTimestamp"), historyFilterDto.getStartDate()));
            }
            if (historyFilterDto.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThan(requestResponseJoin.get("merchantTimestamp"), historyFilterDto.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

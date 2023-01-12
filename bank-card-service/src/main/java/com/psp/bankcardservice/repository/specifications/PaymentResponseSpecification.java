package com.psp.bankcardservice.repository.specifications;


import com.psp.bankcardservice.dto.HistoryFilterDto;
import com.psp.bankcardservice.model.PaymentRequest;
import com.psp.bankcardservice.model.PaymentResponse;
import com.psp.bankcardservice.model.TransactionStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PaymentResponseSpecification {
    public static Specification<PaymentResponse> getFilteredResponses(HistoryFilterDto historyFilterDto) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<PaymentRequest, PaymentResponse> requestResponseJoin = root.join("paymentRequest");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("merchantId"), historyFilterDto.getMerchantId()));

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

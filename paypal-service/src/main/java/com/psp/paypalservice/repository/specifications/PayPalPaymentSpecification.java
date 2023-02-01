package com.psp.paypalservice.repository.specifications;

import com.psp.paypalservice.dto.HistoryFilterDto;
import com.psp.paypalservice.model.PayPalPayment;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
public class PayPalPaymentSpecification {

    public static Specification<PayPalPayment> getFilteredResponses(HistoryFilterDto historyFilterDto) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("merchantId"), historyFilterDto.getCredentialsId()));

            if (historyFilterDto.getStatus() != null && !historyFilterDto.getStatus().equals("")) {
                predicates.add(criteriaBuilder.equal(root.get("state"), historyFilterDto.getStatus()));
            }
            if (historyFilterDto.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("timestamp"), historyFilterDto.getStartDate()));
            }
            if (historyFilterDto.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("timestamp"), historyFilterDto.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

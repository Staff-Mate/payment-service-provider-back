package com.psp.authservice.repository.specification;

import com.psp.authservice.dto.UserFilterDto;
import com.psp.authservice.model.EnabledPaymentMethod;
import com.psp.authservice.model.PaymentMethod;
import com.psp.authservice.model.RegularUser;
import com.psp.authservice.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserSpecification {
    public static Specification<RegularUser> getFilteredUsers(UserFilterDto userFilterDto) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (userFilterDto.getServiceId() != null && !userFilterDto.getServiceId().equals("")) {
                Join<RegularUser, EnabledPaymentMethod> regularUserEnabledPaymentMethodJoin = root.join("enabledPaymentMethods");
                Join<PaymentMethod, EnabledPaymentMethod> paymentMethodEnabledPaymentMethodJoin = regularUserEnabledPaymentMethodJoin.join("paymentMethod");
                predicates.add(criteriaBuilder.equal(paymentMethodEnabledPaymentMethodJoin.get("id"), UUID.fromString(userFilterDto.getServiceId())));
            }

            if (userFilterDto.getSearch() != null && !userFilterDto.getSearch().equals("")) {
                String trimmed = "%" + userFilterDto.getSearch().trim().toLowerCase() + "%";
                Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), trimmed),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), trimmed),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), trimmed));
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

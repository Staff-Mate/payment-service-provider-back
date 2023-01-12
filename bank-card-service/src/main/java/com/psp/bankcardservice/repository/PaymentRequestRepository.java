package com.psp.bankcardservice.repository;

import com.psp.bankcardservice.model.PaymentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, UUID> {

    PaymentRequest findByMerchantOrderId(String merchantOrderId);

    Page<PaymentRequest> findByMerchantIdAndActive(String merchantId, boolean active, Pageable pageable);

}

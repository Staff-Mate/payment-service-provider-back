package com.psp.qrcodeservice.repository;

import com.psp.qrcodeservice.model.PaymentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, UUID> {

    Page<PaymentRequest> findByMerchantIdAndActive(String merchantId, boolean b, Pageable pageable);

    PaymentRequest findByMerchantOrderId(String merchantOrderId);
}

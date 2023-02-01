package com.psp.paypalservice.repository;

import com.psp.paypalservice.model.PayPalPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface PaypalPaymentRepository extends JpaRepository<PayPalPayment, UUID>, JpaSpecificationExecutor<PayPalPayment> {

    PayPalPayment findByPaymentId(String paymentId);

    Page<PayPalPayment> findByMerchantId(String merchantId, Pageable pageable);

}
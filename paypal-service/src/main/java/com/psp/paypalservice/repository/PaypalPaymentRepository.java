package com.psp.paypalservice.repository;

import com.psp.paypalservice.model.PayPalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaypalPaymentRepository extends JpaRepository<PayPalPayment, UUID> {

    PayPalPayment findByPaymentId(String paymentId);

    // Page<PaymentRequest> findByMerchantIdAndActive(String merchantId, boolean active, Pageable pageable);

}
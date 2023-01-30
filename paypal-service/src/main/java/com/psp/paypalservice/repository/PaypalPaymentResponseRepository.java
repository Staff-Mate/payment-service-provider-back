package com.psp.paypalservice.repository;

import com.psp.paypalservice.model.PayPalPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaypalPaymentResponseRepository extends JpaRepository<PayPalPaymentResponse, UUID> {

}
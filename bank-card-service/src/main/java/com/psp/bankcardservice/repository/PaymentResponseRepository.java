package com.psp.bankcardservice.repository;

import com.psp.bankcardservice.model.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentResponseRepository extends JpaRepository<PaymentResponse, UUID>, JpaSpecificationExecutor<PaymentResponse> {
}
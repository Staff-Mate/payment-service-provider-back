package com.psp.authservice.repository;

import com.psp.authservice.model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, UUID> {

    PaymentAttempt findPaymentAttemptById(UUID id);
}

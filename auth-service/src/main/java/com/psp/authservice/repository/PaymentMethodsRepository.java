package com.psp.authservice.repository;

import com.psp.authservice.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethod, UUID> {
}

package com.psp.authservice.repository;

import com.psp.authservice.model.EnabledPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnabledPaymentMethodRepository extends JpaRepository<EnabledPaymentMethod, UUID> {
}

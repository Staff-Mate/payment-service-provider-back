package com.psp.paypalservice.repository;

import com.psp.paypalservice.model.PayPalSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaypalSubscriptionRepository extends JpaRepository<PayPalSubscription, UUID> {

}

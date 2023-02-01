package com.psp.bitcoinservice.repository;

import com.psp.bitcoinservice.model.BtcPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BtcPaymentResponseRepository extends JpaRepository<BtcPaymentResponse, UUID> {
}

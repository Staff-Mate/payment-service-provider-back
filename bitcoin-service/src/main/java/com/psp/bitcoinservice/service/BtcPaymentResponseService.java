package com.psp.bitcoinservice.service;

import com.psp.bitcoinservice.model.BtcPaymentResponse;
import com.psp.bitcoinservice.repository.BtcPaymentResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BtcPaymentResponseService {
    @Autowired
    private BtcPaymentResponseRepository btcPaymentResponseRepository;

    public void save(BtcPaymentResponse btcPaymentResponse) {
        btcPaymentResponseRepository.save(btcPaymentResponse);
    }
}

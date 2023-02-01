package com.psp.paypalservice.service;

import com.paypal.api.payments.Payment;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.model.PayPalPayment;
import com.psp.paypalservice.repository.PaypalPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaypalPaymentService {

    @Autowired
    private PaypalPaymentRepository paypalPaymentRepository;

    public void savePayment(Payment payment, ServicePaymentDto servicePaymentDto, String type) {
        PayPalPayment copy = new PayPalPayment();
        copy.setPaymentId(payment.getId());
        copy.setMerchantId(servicePaymentDto.getCredentialsId());
        copy.setSuccessUrl(servicePaymentDto.getSuccessUrl());
        copy.setErrorUrl(servicePaymentDto.getErrorUrl());
        copy.setFailedUrl(servicePaymentDto.getFailedUrl());
        copy.setState(payment.getState());
        copy.setAmount(payment.getTransactions().get(0).getAmount().getTotal());
        copy.setTimestamp(payment.getCreateTime());
        copy.setType(type);
        paypalPaymentRepository.save(copy);
    }

    public PayPalPayment getById(String id){
        return paypalPaymentRepository.findByPaymentId(id);
    }

    public PayPalPayment updatePayment(Payment payment) {
        PayPalPayment saved = paypalPaymentRepository.findByPaymentId(payment.getId());
        saved.setState(payment.getState());
        saved.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
        saved.setTimestamp(payment.getUpdateTime());
        return paypalPaymentRepository.save(saved);
    }

}

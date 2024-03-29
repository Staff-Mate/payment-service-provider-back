package com.psp.paypalservice.service;

import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.Payment;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.model.PayPalPayment;
import com.psp.paypalservice.model.PayPalSubscription;
import com.psp.paypalservice.repository.PaypalPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class PayPalPaymentService {

    @Autowired
    private PaypalPaymentRepository paypalPaymentRepository;

    public void savePayment(Payment payment, ServicePaymentDto servicePaymentDto, String type) throws ParseException {
        PayPalPayment copy = new PayPalPayment();
        copy.setPaymentId(payment.getId());
        copy.setMerchantId(servicePaymentDto.getCredentialsId());
        copy.setSuccessUrl(servicePaymentDto.getSuccessUrl());
        copy.setErrorUrl(servicePaymentDto.getErrorUrl());
        copy.setFailedUrl(servicePaymentDto.getFailedUrl());
        copy.setState(payment.getState());
        copy.setAmount(payment.getTransactions().get(0).getAmount().getTotal());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        copy.setTimestamp(new Timestamp(sdf.parse(payment.getCreateTime()).getTime())); //to timestamp


        copy.setType(type);
        paypalPaymentRepository.save(copy);

        log.debug("PayPal payment with id: {} created. Merchant id: {}, Amount: {}",
                copy.getPaymentId(),copy.getMerchantId(), copy.getAmount());
    }

    public PayPalPayment getById(String id){
        return paypalPaymentRepository.findByPaymentId(id);
    }

    public PayPalPayment updatePayment(Payment payment) throws ParseException {
        PayPalPayment saved = paypalPaymentRepository.findByPaymentId(payment.getId());
        saved.setState(payment.getState());
        saved.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        saved.setTimestamp(new Timestamp(sdf.parse(payment.getUpdateTime()).getTime()));

        PayPalPayment updated = paypalPaymentRepository.save(saved);
        log.debug("PayPal payment with id: {} updated. Payer id: {}, New state: {}",
                saved.getPaymentId(),saved.getPayerId(), saved.getState());

        return updated;
    }

    public void saveSetupPayment(Agreement agreement, PayPalSubscription sub) {
        PayPalPayment payment = new PayPalPayment();
        payment.setAmount(agreement.getPlan().getMerchantPreferences().getSetupFee().getValue());
        payment.setState("approved");
        payment.setMerchantId(sub.getMerchantId());
        payment.setType("SETUP_FEE");

        payment.setTimestamp(new Timestamp((new Date()).getTime()));

        paypalPaymentRepository.save(payment);

        log.debug("PayPal SETUP_FEE payment created. Merchant id: {}, Amount: {}",
                payment.getMerchantId(), payment.getAmount());
    }
}

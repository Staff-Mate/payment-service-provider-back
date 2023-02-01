package com.psp.paypalservice.service;

import com.paypal.api.payments.Agreement;
import com.paypal.api.payments.Plan;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.model.PayPalSubscription;
import com.psp.paypalservice.repository.PaypalSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayPalSubscriptionService {

    @Autowired
    private PaypalSubscriptionRepository paypalSubscriptionRepository;

    public void saveSubscription(Plan plan, Agreement agreement, ServicePaymentDto servicePaymentDto){
        PayPalSubscription sub = new PayPalSubscription();

        sub.setPlanId(plan.getId());
        sub.setCycles(plan.getPaymentDefinitions().get(0).getCycles());
        sub.setBilling(plan.getPaymentDefinitions().get(0).getFrequency());
        sub.setSetupFee(Double.parseDouble(plan.getMerchantPreferences().getSetupFee().getValue()));

        sub.setSuccessUrl(servicePaymentDto.getSuccessUrl());
        sub.setFailedUrl(servicePaymentDto.getFailedUrl());
        sub.setErrorUrl(servicePaymentDto.getErrorUrl());
        sub.setMerchantId(servicePaymentDto.getCredentialsId());

        sub.setAgreementToken(agreement.getToken());
        sub.setAgreementStartDate(agreement.getStartDate());
        sub.setAgreementName(agreement.getName());
        sub.setAgreementState("Created");

        paypalSubscriptionRepository.save(sub);
    }

    public PayPalSubscription getByAgreementToken(String token) {
        return paypalSubscriptionRepository.findByAgreementToken(token);
    }

    public void updateSubscription(Agreement agreement, String token) {
        PayPalSubscription saved = paypalSubscriptionRepository.findByAgreementToken(token);
        saved.setAgreementState(agreement.getState());
        saved.setAgreementId(agreement.getId());
        paypalSubscriptionRepository.save(saved);
    }
}

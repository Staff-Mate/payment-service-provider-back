package com.psp.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.model.PayPalPayment;
import com.psp.paypalservice.model.PayPalSubscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class PaymentRequestService {

    @Autowired
    private PayPalSubscriptionService payPalSubscriptionService;

    @Autowired
    private PayPalPaymentService paypalPaymentService;

    @Autowired
    private RestTemplate restTemplate;

    private APIContext apiContext;

    @Value("${paypal.mode}")
    private String mode;

    public ResponseEntity<?> createPayment(ServicePaymentDto servicePaymentDto) {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        RedirectUrls urls = new RedirectUrls();
        urls.setCancelUrl(servicePaymentDto.getFailedUrl());
        urls.setReturnUrl("http://localhost:9200/payment-requests/success");

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(Double.toString(servicePaymentDto.getAmount()));
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(urls);
        payment.setTransactions(transactions);
        apiContext = new APIContext(servicePaymentDto.getCredentialsId(),
                servicePaymentDto.getCredentialsSecret(),mode);

        try{
            payment = payment.create(apiContext);
            if(payment.getState().equals("created")) {
                paypalPaymentService.savePayment(payment, servicePaymentDto, servicePaymentDto.getBillingCycle());
                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        log.debug("Payment created, needs to be executed. Merchant id: {}, Amount: {}",
                                servicePaymentDto.getCredentialsId(), payment.getTransactions().get(0).getAmount().getTotal());
                        return new ResponseEntity<>(link.getHref(), HttpStatus.OK);
                    }
                }
            }
            log.warn("Payment failed to be created.  Merchant id: {}, Amount: {}",
                    servicePaymentDto.getCredentialsId(), payment.getTransactions().get(0).getAmount().getTotal());
            return new ResponseEntity<>(servicePaymentDto.getFailedUrl(), HttpStatus.OK);
        } catch(PayPalRESTException e) {
            e.printStackTrace();
            log.warn("Payment creation encountered an exception.  Merchant id: {}, Amount: {}",
                    servicePaymentDto.getCredentialsId(), payment.getTransactions().get(0).getAmount().getTotal());
            return new ResponseEntity<>(servicePaymentDto.getErrorUrl(), HttpStatus.BAD_REQUEST);
        }
    }

    public String executePayment(String paymentId, String token, String payerID) {

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerID);
        PayPalPayment saved = paypalPaymentService.getById(paymentId);
        try{
            payment = payment.execute(apiContext, paymentExecution);
            paypalPaymentService.updatePayment(payment);
            if(payment.getState().equals("approved")){
                log.debug("Payment with id: {} successfully executed. New state: {}", payment.getId(), payment.getState());
                return  saved.getSuccessUrl();
            }
            log.warn("Payment with id: {} failed to be executed.", payment.getId());
            return saved.getFailedUrl();
        } catch(PayPalRESTException e) {
            log.warn("Payment with id: {} encountered an exception while being executed.", payment.getId());
            e.printStackTrace();
            return saved.getErrorUrl();
        }
    }

    public ResponseEntity<?> createSubscription(ServicePaymentDto servicePaymentDto) throws PayPalRESTException {

        Plan plan = createPlan(servicePaymentDto);
        Agreement agreement = createAgreement(servicePaymentDto, plan.getId());

        if(agreement == null) {
            log.warn("Agreement for Plan with id: {} failed to be created. Returned null.", plan.getId());
            return new ResponseEntity<>(servicePaymentDto.getErrorUrl(), HttpStatus.BAD_REQUEST);
        }
        payPalSubscriptionService.saveSubscription(plan, agreement, servicePaymentDto);
        for(Links link : agreement.getLinks()){
            if(link.getRel().equals("approval_url")){
                log.debug("Subscription successfully created. Plan id: {}, Agreement token: {}, Billing Cycle: {}, Amount: {}",
                        plan.getId(), agreement.getToken(), plan.getPaymentDefinitions().get(0).getFrequency(),
                        plan.getPaymentDefinitions().get(0).getAmount().getValue());
                return new ResponseEntity<>(link.getHref(), HttpStatus.OK);
            }
        }
        log.debug("Subscription failed to be created. Plan id: {}, Agreement token: {}, Billing Cycle: {}, Amount: {}",
                plan.getId(), agreement.getToken(), plan.getPaymentDefinitions().get(0).getFrequency(),
                plan.getPaymentDefinitions().get(0).getAmount().getValue());
        return new ResponseEntity<>(servicePaymentDto.getFailedUrl(), HttpStatus.OK);
    }

    private Plan createPlan(ServicePaymentDto servicePaymentDto) {

        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setReturnUrl("http://localhost:9200/payment-requests/subsuccess");
        merchantPreferences.setCancelUrl(servicePaymentDto.getFailedUrl());
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setSetupFee(new Currency("USD", "1.05"));

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setAmount(new Currency("USD", servicePaymentDto.getAmount().toString()));
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency(servicePaymentDto.getBillingCycle());
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles("12");
        paymentDefinition.setName("12 payments");
        List<PaymentDefinition> paymentDefinitions = new ArrayList<>();
        paymentDefinitions.add(paymentDefinition);

        Plan plan = new Plan();
        plan.setMerchantPreferences(merchantPreferences);
        plan.setPaymentDefinitions(paymentDefinitions);
        plan.setState("ACTIVE");
        plan.setType("fixed");
        plan.setName(servicePaymentDto.getBillingCycle() + "LY  PLAN");
        plan.setDescription("Subscription plan");
        try{
            apiContext = new APIContext(servicePaymentDto.getCredentialsId(), servicePaymentDto.getCredentialsSecret(), mode);
            plan = plan.create(apiContext);
        }catch (Exception e){
            log.warn("Plan could not be created. Name: {}, Amount: {}",
                    plan.getName(), plan.getPaymentDefinitions().get(0).getAmount().getValue());
            e.printStackTrace();
        }

        Map<String, String> changes = new HashMap<>();
        changes.put("state", "ACTIVE");
        Patch patch = new Patch();
        patch.setValue(changes);
        patch.setOp("replace");
        patch.setPath("/");
        List<Patch> patches = new ArrayList<>();
        patches.add(patch);
        try{
            plan.update(apiContext, patches);
        }catch (Exception e){
            log.warn("Plan with id: {} could not be activated. Name: {}, Amount: {}",
                    plan.getId(), plan.getName(), plan.getPaymentDefinitions().get(0).getAmount().getValue());
            e.printStackTrace();
        }
        log.debug("Plan with id: {} created and activated. Name: {}, Amount: {}", plan.getId(),
                plan.getName(), plan.getPaymentDefinitions().get(0).getAmount().getValue());
        return plan;
    }

    private Agreement createAgreement(ServicePaymentDto servicePaymentDto, String planId) {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Plan plan = new Plan();
        plan.setId(planId);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Agreement agreement = new Agreement();
        agreement.setStartDate(sdf.format(calendar.getTime()));
        agreement.setPlan(plan);
        agreement.setPayer(payer);
        agreement.setName("Billing agreement");
        agreement.setDescription("Agreement for subscription");

        try{
            agreement = agreement.create(apiContext);
            log.debug("Agreement with token: {} successfully created. Plan id: {}, Start date: {}",
                    agreement.getToken(), agreement.getPlan().getId(), agreement.getStartDate());
            return agreement;
        }catch(Exception e){
            log.warn("Agreement could not be created. Plan id: {}",
                     agreement.getPlan().getId());
            e.printStackTrace();
            return null;
        }
    }

    public String executeAgreement(String token, String ba_token) {

        PayPalSubscription saved = payPalSubscriptionService.getByAgreementToken(token);
        Agreement agreement = new Agreement();
        try{
            agreement =  agreement.execute(apiContext, token);
            if(agreement == null) {
                log.warn("Agreement with token: {} could not be executed", agreement.getToken());
                return saved.getFailedUrl();
            }
            payPalSubscriptionService.updateSubscription(agreement, token);
            paypalPaymentService.saveSetupPayment(agreement, saved);
            log.debug("Agreement with token: {} executed successfully. Subscription is now valid. Merchant id: {}, Plan id: {}",
                    saved.getAgreementToken(), saved.getMerchantId(), saved.getPlanId());
            return saved.getSuccessUrl();
        }catch(Exception e){
            e.printStackTrace();
            log.debug("Agreement with token: {} failed to execute. Merchant id: {}, Plan id: {}",
                    saved.getAgreementToken(), saved.getMerchantId(), saved.getPlanId());
            return saved.getErrorUrl();
        }
    }
}
package com.psp.paypalservice.service;

import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.psp.paypalservice.dto.ServicePaymentDto;
import com.psp.paypalservice.repository.PaypalPaymentRepository;
import com.psp.paypalservice.repository.PaypalSubscriptionRepository;
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
    private PaypalSubscriptionRepository paypalSubscriptionRepository;

    @Autowired
    private PaypalPaymentRepository paypalPaymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    private APIContext apiContext;

    @Value("${service.front.url}")
    private String frontUrl;

    @Value("${paypal.mode}")
    private String mode;

    public ResponseEntity<?> createPayment(ServicePaymentDto servicePaymentDto) {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        RedirectUrls urls = new RedirectUrls();
        urls.setCancelUrl(servicePaymentDto.getFailedUrl()); // ili error url?
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
                //snimiti
                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        return new ResponseEntity<String>(link.getHref(), HttpStatus.OK);
                    }
                }
            }
        } catch(PayPalRESTException e) {
            //log
            e.printStackTrace();
            return new ResponseEntity<String>(servicePaymentDto.getErrorUrl(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(servicePaymentDto.getFailedUrl(), HttpStatus.BAD_REQUEST);
    }

    //https://example.com/return?paymentId=PAYID-MPMOTVA82587878KX934172W&token=EC-64689213Y9451631J&PayerID=8QV4E6BDM47D6
//    DOBILA SAM  MOJ SUCCESS URL SA PARAMETRIMA PAYMENT ID i TOKEN i PAYERID

    public ResponseEntity<?> executePayment(String paymentId, String token, String payerID) {

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerID);
        try{

            payment = payment.execute(apiContext, paymentExecution);

            if(payment.getState().equals("approved")){
                savePayment(payment);
                System.out.println("___________USPEHHHHHHHHHHHHHH");
                //VAdimo payment iz baze i saljemo success url
                return  new ResponseEntity<String>("<h1>uspeh</h1>", HttpStatus.OK);
            }
        } catch(PayPalRESTException e) {
            //log
            e.printStackTrace();
            //VAdimo payment iz baze i saljemo error url
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
        //VAdimo payment iz baze i saljemo fail url
        return null;

    }

    private void savePayment(Payment payment) {
        System.out.println("Henlo\n\n");
//        PAYMENT RESPONSE SACUVATI?
    }

    public ResponseEntity<?> createSubscription(ServicePaymentDto servicePaymentDto) throws PayPalRESTException {
       // Create plan
        Plan plan = createPlan(servicePaymentDto);

        // Create Agreement
        Agreement agreement = createAgreement(servicePaymentDto, plan.getId());
        if(agreement == null) {
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
        for(Links link : agreement.getLinks()){
            if(link.getRel().equals("approval_url")){
                return new ResponseEntity<String>(link.getHref(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
    }

//    http://localhost:9200/payment-requests/success?token=EC-4S491563R4469123A&ba_token=BA-43Y92145GD2251927

    private Plan createPlan(ServicePaymentDto servicePaymentDto) throws PayPalRESTException {
        // Merchant preference
        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setReturnUrl(servicePaymentDto.getSuccessUrl());    //TREBA DA IMA execute putanju, ili success
        merchantPreferences.setCancelUrl(servicePaymentDto.getFailedUrl());
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setSetupFee(new Currency("USD", "1.11"));

        // Payment definitions
        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setAmount(new Currency("USD", servicePaymentDto.getAmount().toString()));
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency(servicePaymentDto.getBillingCycle());
        paymentDefinition.setFrequencyInterval("1");
        paymentDefinition.setCycles("12");
        paymentDefinition.setName("12 payments");

        List<PaymentDefinition> paymentDefinitions = new ArrayList<>();
        paymentDefinitions.add(paymentDefinition);

        // Create billing plan
        Plan plan = new Plan();
        plan.setMerchantPreferences(merchantPreferences);
        plan.setPaymentDefinitions(paymentDefinitions);
        plan.setState("ACTIVE");
        plan.setType("fixed");
//        plan.setType("INFINITE"); v1
        plan.setName(servicePaymentDto.getBillingCycle() + "LY  PLAN");
        plan.setDescription("Subscription plan");


        try{
            apiContext = new APIContext(servicePaymentDto.getCredentialsId(),
                    servicePaymentDto.getCredentialsSecret(),"sandbox");
            plan = plan.create(apiContext);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("USPEH");

        // activate plan
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
            e.printStackTrace();
        }

        System.out.println("---------------------AKT TI VACIJA");
        System.out.println(plan.getState());

        return plan;
    }

    private Agreement createAgreement(ServicePaymentDto servicePaymentDto, String planId) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Plan plan = new Plan(); //!!!!!!!!!!!!!!!!!!!
        plan.setId(planId);

        Agreement agreement = new Agreement();
        agreement.setStartDate(sdf.format(calendar.getTime()));
        agreement.setPlan(plan);
        agreement.setPayer(payer);
        agreement.setName("Billing agreement");
        agreement.setDescription("Agreement for subscription");

        try{
            agreement = agreement.create(apiContext);
            //save
            //log
            return agreement;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> executeAgreement(String token, String ba_token) {

        Agreement agreement = new Agreement();
        try{
            agreement =  agreement.execute(apiContext, token);

            return new ResponseEntity<>("<h1>IS IT SUCCESS?? : "+ agreement.getState() +"execute"+ token + ba_token +"</h1>", HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }


        return new ResponseEntity<>("<h1>NE VALJA </h1>", HttpStatus.BAD_REQUEST);
    }

    private void saveSubscription(Agreement agreement, Plan plan) {
        System.out.println("Henlo\n\n");
//        PAYMENT RESPONSE SACUVATI?
    }


}
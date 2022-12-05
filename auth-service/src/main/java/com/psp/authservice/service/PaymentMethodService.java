package com.psp.authservice.service;

import com.psp.authservice.dto.PaymentMethodDto;
import com.psp.authservice.model.PaymentMethod;
import com.psp.authservice.repository.PaymentMethodRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PaymentMethod findPaymentMethodById(UUID paymentMethodUUID) {
        return paymentMethodRepository.findPaymentMethodById(paymentMethodUUID);
    }

    public ResponseEntity<?> getPaymentMethod(UUID paymentMethodUUID) {
        PaymentMethod paymentMethod = findPaymentMethodById(paymentMethodUUID);
        PaymentMethodDto paymentMethodDto = modelMapper.map(paymentMethod, PaymentMethodDto.class);
        return new ResponseEntity<>(paymentMethodDto, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        List<PaymentMethodDto> paymentMethodDtos = paymentMethods.stream().map((entity) -> modelMapper.map(entity, PaymentMethodDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(paymentMethodDtos, HttpStatus.OK);
    }

    public ResponseEntity<?> addPaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        log.debug("Payment method '{}' added, with id: {}", paymentMethod.getName(), paymentMethod.getId());
        return new ResponseEntity<>(paymentMethod, HttpStatus.CREATED);
    }

    public ResponseEntity<?> deletePaymentMethod(UUID paymentMethodUUID) {
        PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodById(paymentMethodUUID);
        paymentMethodRepository.delete(paymentMethod);
        log.debug("Payment method with id: {} deleted.", paymentMethod.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> updatePaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDto, PaymentMethod.class);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        log.debug("Payment method with id: {} updated.", paymentMethod.getId());
        return new ResponseEntity<>(paymentMethod, HttpStatus.OK);
    }
}

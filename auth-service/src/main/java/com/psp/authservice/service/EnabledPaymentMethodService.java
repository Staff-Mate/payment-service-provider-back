package com.psp.authservice.service;

import com.psp.authservice.model.EnabledPaymentMethod;
import com.psp.authservice.repository.EnabledPaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnabledPaymentMethodService {

    @Autowired
    private EnabledPaymentMethodRepository enabledPaymentMethodRepository;

    public EnabledPaymentMethod save(EnabledPaymentMethod enabledPaymentMethod) {
        return enabledPaymentMethodRepository.save(enabledPaymentMethod);
    }

    public EnabledPaymentMethod findById(UUID id) {
        return enabledPaymentMethodRepository.findEnabledPaymentMethodById(id);
    }

    public void delete(EnabledPaymentMethod enabledPaymentMethod) {
        enabledPaymentMethodRepository.delete(enabledPaymentMethod);
    }
}

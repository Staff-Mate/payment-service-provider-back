package com.psp.authservice.service;

import com.psp.authservice.dto.EnabledPaymentMethodDto;
import com.psp.authservice.model.EnabledPaymentMethod;
import com.psp.authservice.model.PaymentMethod;
import com.psp.authservice.model.RegularUser;
import com.psp.authservice.model.User;
import com.psp.authservice.repository.EnabledPaymentMethodRepository;
import com.psp.authservice.repository.RegularUserRepository;
import com.psp.authservice.repository.UserRepository;
import com.psp.authservice.security.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private EnabledPaymentMethodRepository enabledPaymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EnabledPaymentMethodService enabledPaymentMethodService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    public User getUserFromToken(String token) {
        String email = tokenUtils.getEmailFromToken(token);
        return userRepository.findByEmail(email);
    }

    public Boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public RegularUser findUserByApiKey(String apiKey) {
        return regularUserRepository.findByApiKey(apiKey);
    }

    public ResponseEntity<?> getAllPaymentMethodsForCompany(String userEmail) {
        RegularUser user = (RegularUser) userRepository.findByEmail(userEmail);
        List<EnabledPaymentMethodDto> userPaymentMethods = new ArrayList<>();
        for (EnabledPaymentMethod enabledPaymentMethod : user.getEnabledPaymentMethods()) {
            userPaymentMethods.add(modelMapper.map(enabledPaymentMethod, EnabledPaymentMethodDto.class));
        }
        return new ResponseEntity<>(userPaymentMethods, HttpStatus.OK);
    }


    public ResponseEntity<?> getAllPaymentMethodsForApiKey(String apiKey) {
        RegularUser user = regularUserRepository.findByApiKey(apiKey);
        List<EnabledPaymentMethodDto> userPaymentMethods = new ArrayList<>();
        for (EnabledPaymentMethod enabledPaymentMethod : user.getEnabledPaymentMethods()) {
            userPaymentMethods.add(modelMapper.map(enabledPaymentMethod, EnabledPaymentMethodDto.class));
        }
        return new ResponseEntity<>(userPaymentMethods, HttpStatus.OK);
    }

    public ResponseEntity<?> addPaymentMethodForCompany(String userEmail, EnabledPaymentMethodDto enabledPaymentMethodDto) {
        RegularUser user = (RegularUser) userRepository.findByEmail(userEmail);
        EnabledPaymentMethod enabledPaymentMethod = modelMapper.map(enabledPaymentMethodDto, EnabledPaymentMethod.class);
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(enabledPaymentMethodDto.getPaymentMethod().getId());
        if (getPaymentMethodForCompany(user, paymentMethod.getId()) == null) {
            enabledPaymentMethod.setPaymentMethod(paymentMethod);
            List<EnabledPaymentMethod> enabledPaymentMethods = enablePaymentMethodForCompany(user, enabledPaymentMethod);
            log.debug("Payment method with id: {}, enabled for merchant: {}", paymentMethod.getId(), user.getId());
            return new ResponseEntity<>(enabledPaymentMethods.stream().map(enabledPayment -> modelMapper.map(enabledPayment, EnabledPaymentMethodDto.class)), HttpStatus.CREATED);
        } else {
            log.warn("Payment method with id: {} is already enabled for merchant: {}", paymentMethod.getId(), user.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<EnabledPaymentMethod> enablePaymentMethodForCompany(RegularUser user, EnabledPaymentMethod enabledPaymentMethod) {
        enabledPaymentMethod = enabledPaymentMethodService.save(enabledPaymentMethod);
        List<EnabledPaymentMethod> userEnabledPaymentMethods = user.getEnabledPaymentMethods();
        userEnabledPaymentMethods.add(enabledPaymentMethod);
        user.setEnabledPaymentMethods(userEnabledPaymentMethods);
        userRepository.save(user);
        return user.getEnabledPaymentMethods();
    }

    public ResponseEntity<?> deleteEnabledPaymentMethod(String userEmail, UUID enabledPaymentMethodId) {
        RegularUser user = (RegularUser) userRepository.findByEmail(userEmail);
        if (isPaymentMethodEnabledForCompany(user, enabledPaymentMethodId)) {
            List<EnabledPaymentMethod> enabledPaymentMethods = deleteEnabledPaymentMethod(enabledPaymentMethodId, user);
            log.debug("Enabled payment method option with id: {}, deleted from merchant: {}", enabledPaymentMethodId, user.getId());
            return new ResponseEntity<>(enabledPaymentMethods.stream().map(enabledPaymentMethod -> modelMapper.map(enabledPaymentMethod, EnabledPaymentMethodDto.class)), HttpStatus.OK);
        }
        log.debug("Merchant {} cannot delete payment method option with id: {} - no payment option with given id", user.getId(), enabledPaymentMethodId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private List<EnabledPaymentMethod> deleteEnabledPaymentMethod(UUID enabledPaymentMethodId, RegularUser user) {
        EnabledPaymentMethod enabledPaymentMethod = enabledPaymentMethodService.findById(enabledPaymentMethodId);
        for (int i = 0; i < user.getEnabledPaymentMethods().size(); i++) {
            if (enabledPaymentMethod.getId().equals(user.getEnabledPaymentMethods().get(i).getId())) {
                user.getEnabledPaymentMethods().remove(user.getEnabledPaymentMethods().get(i));
                userRepository.save(user);
                enabledPaymentMethodService.delete(enabledPaymentMethod);
                return user.getEnabledPaymentMethods();
            }
        }
        return null;
    }

    private boolean isPaymentMethodEnabledForCompany(RegularUser user, UUID enabledPaymentMethodId) {
        for (EnabledPaymentMethod enabledPaymentMethod : user.getEnabledPaymentMethods()) {
            if (enabledPaymentMethod.getId().equals(enabledPaymentMethodId)) {
                return true;
            }
        }
        return false;
    }

    public EnabledPaymentMethod getPaymentMethodForCompany(RegularUser user, UUID paymentMethodId) {
        for (EnabledPaymentMethod enabledPaymentMethod : user.getEnabledPaymentMethods()) {
            if (enabledPaymentMethod.getPaymentMethod().getId().equals(paymentMethodId)) {
                return enabledPaymentMethod;
            }
        }
        return null;
    }

}

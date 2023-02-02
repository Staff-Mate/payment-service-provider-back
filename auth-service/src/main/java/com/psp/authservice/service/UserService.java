package com.psp.authservice.service;

import com.psp.authservice.dto.*;
import com.psp.authservice.model.*;
import com.psp.authservice.repository.*;
import com.psp.authservice.repository.specification.UserSpecification;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private BankService bankService;

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
    private PaymentAttemptService paymentAttemptService;

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


    public ResponseEntity<?> getAllPaymentMethodsByPaymentAttemptId(UUID paymentAttemptId) {
        PaymentAttempt paymentAttempt = paymentAttemptService.getPaymentAttempt(paymentAttemptId);
        RegularUser user = regularUserRepository.findByApiKey(paymentAttempt.getApiKey());
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
        regularUserRepository.save(user);
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
                regularUserRepository.save(user);
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

    public ResponseEntity<?> updateProfile(String userEmail, UserDto userDto) {
        RegularUser user = (RegularUser) userRepository.findByEmail(userEmail);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBank(bankService.getBankById(userDto.getBank().getId()));
        user.setSuccessUrl(userDto.getSuccessUrl());
        user.setErrorUrl(userDto.getErrorUrl());
        user.setFailedUrl(userDto.getFailedUrl());
        regularUserRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getAllUsers() {
        List<User> users = regularUserRepository.findAllByRole_Name("ROLE_USER");
        List<RegularUser> regularUsers = users.stream().map(user -> (RegularUser) user).collect(Collectors.toList());
        return new ResponseEntity<>(mapToRegularUserDto(regularUsers),HttpStatus.OK);
    }

    private List<RegularUserDto> mapToRegularUserDto(List<RegularUser> regularUsers) {
        return regularUsers.stream().map(regularUser -> {
            RegularUserDto regularUserDto = modelMapper.map(regularUser, RegularUserDto.class);
            List<PaymentMethod> paymentMethods = regularUser.getEnabledPaymentMethods().stream().map(EnabledPaymentMethod::getPaymentMethod).collect(Collectors.toList());
            List<PaymentMethodDto> paymentMethodDtoList = paymentMethods.stream().map(paymentMethod -> modelMapper.map(paymentMethod,PaymentMethodDto.class)).collect(Collectors.toList());
            regularUserDto.setPaymentMethods(paymentMethodDtoList);
            return regularUserDto;
        }).collect(Collectors.toList());
    }

    public ResponseEntity<?> getFilteredUsers(UserFilterDto userFilterDto) {
        List<RegularUser> regularUsers = regularUserRepository.findAll(UserSpecification.getFilteredUsers(userFilterDto));
        return new ResponseEntity<>(mapToRegularUserDto(regularUsers),HttpStatus.OK);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

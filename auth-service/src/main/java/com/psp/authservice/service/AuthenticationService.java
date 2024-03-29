package com.psp.authservice.service;

import com.psp.authservice.dto.LoggedInUserDto;
import com.psp.authservice.dto.PasswordDto;
import com.psp.authservice.dto.UserDto;
import com.psp.authservice.model.ConfirmationToken;
import com.psp.authservice.model.RegularUser;
import com.psp.authservice.model.User;
import com.psp.authservice.security.exception.ResourceConflictException;
import com.psp.authservice.security.util.JwtAuthenticationRequest;
import com.psp.authservice.security.util.TokenUtils;
import com.psp.authservice.security.util.UserTokenState;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BankService bankService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (!user.getActivated()) {
            return null;
        }
        return getAuthentication(user);
    }

    public UserTokenState getAuthentication(User user) {
        return new UserTokenState(tokenUtils.generateToken(user.getEmail(), user.getRole().getAuthority(), user.getRole().getPermissionNames()), tokenUtils.getExpiredIn(), getRoles(user));
    }

    public List<String> getRoles(User user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public boolean confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken != null ) {
            User user = userService.getUserByEmail(confirmationToken.getEmail());
            if(user == null)
                return false;
            user.setActivated(true);
            userService.saveUser(user);
            confirmationTokenService.deleteToken(confirmationToken);
            return true;
        }
        return false;
    }
    public ResponseEntity<?> signUp(UserDto userDto) throws ResourceConflictException, MessagingException, TemplateException, IOException {
        RegularUser user = modelMapper.map(userDto, RegularUser.class);
        user.setBank(bankService.getBankById(userDto.getBank().getId()));
        user.setRole(roleService.getById(1));
        if (userService.isEmailRegistered(user.getEmail()).equals(true)) {
            log.warn("Already registered email: {} entered in attempted registration.", user.getEmail());
            throw new ResourceConflictException("Email already exists");
        } else {
            if (isPasswordInvalid(userDto.getPassword()) && Objects.equals(userDto.getPassword(), userDto.getConfirmPassword())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setApiKey(tokenUtils.generateAPIToken(user.getEmail()));
            user.setActivated(false);
            userService.saveUser(user);
            try{
                sendRegistrationEmail(user);
            }catch (Exception e){
                log.debug("Email not send.");
            }
            log.debug("User with email: {} registered.", user.getEmail());
            return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.CREATED);
        }
    }
    private void sendRegistrationEmail(User user) throws MessagingException, TemplateException, IOException {
        ConfirmationToken token = confirmationTokenService.generateConfirmationToken(user.getEmail());
        emailService.sendRegistrationEmail(user, token);
        confirmationTokenService.encodeToken(token);
    }
    public ResponseEntity<?> getLoggedInUser(String token) {
        User user = this.userService.getUserFromToken(token);
        LoggedInUserDto dto = new LoggedInUserDto();
        if (user.getClass().equals(RegularUser.class)) {
            dto = modelMapper.map(user, LoggedInUserDto.class);
            dto.setDisplayName(((RegularUser) user).getCompanyName());
        } else {
            dto.setDisplayName("Administrator");
        }
        dto.setPermissions(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        dto.setEmail(user.getEmail());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    public ResponseEntity<?> changePassword(String token, PasswordDto passwordDto) {
        User user = this.userService.getUserFromToken(token);
        if(passwordEncoder.matches(passwordDto.getOldPassword(),user.getPassword()) && passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())){
            if (isPasswordInvalid(passwordDto.getNewPassword())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
            userService.saveUser(user);
            log.debug("User with email: {} changed password.", user.getEmail());
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public boolean isPasswordInvalid(String password){
        if (!Pattern.matches(".*[0-9].*", password)){
            return true;
        }
        if (!Pattern.matches(".*[a-z].*", password)){
            return true;
        }
        if (!Pattern.matches(".*[A-Z].*", password)){
            return true;
        }
        if(!Pattern.matches(".*[!?#$@.*+_].*", password)){
            return true;
        }
        return !Pattern.matches("[0-9A-Za-z!?#$@.*+_]{8,50}", password);
    }
}

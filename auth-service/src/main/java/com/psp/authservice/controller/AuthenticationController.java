package com.psp.authservice.controller;

import com.psp.authservice.dto.UserDto;
import com.psp.authservice.security.exception.ResourceConflictException;
import com.psp.authservice.security.util.JwtAuthenticationRequest;
import com.psp.authservice.security.util.UserTokenState;
import com.psp.authservice.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            UserTokenState userTokenState = authenticationService.login(authenticationRequest);
            return ResponseEntity.ok(userTokenState);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        log.debug("POST request received - /auth/register. User email: {}", userDto.getEmail());
        try {
            return authenticationService.signUp(userDto);
        } catch (ResourceConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getLoggedInUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            if(authorization.equals("")){
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return authenticationService.getLoggedInUser(authorization.split("Bearer ")[1]);
        } catch (ResourceConflictException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

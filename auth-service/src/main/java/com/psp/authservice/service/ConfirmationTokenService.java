package com.psp.authservice.service;


import com.psp.authservice.model.ConfirmationToken;
import com.psp.authservice.repository.ConfirmationTokenRepository;
import com.psp.authservice.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ConfirmationToken saveToken(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken findByToken(String token) {
        for (ConfirmationToken confToken : confirmationTokenRepository.findAll()) {
            if (passwordEncoder.matches(token.substring(token.length() - 10), confToken.getToken()))
                return confToken;
        }
        return null;
    }

    public void deleteToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }

    public ConfirmationToken generateConfirmationToken(String email) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setEmail(email);
        confirmationToken.setToken(tokenUtils.generateToken(email, "ROLE_USER", null));
        saveToken(confirmationToken);
        return confirmationToken;
    }

    public void encodeToken(ConfirmationToken token) {
        token.setToken(passwordEncoder.encode(token.getToken().substring(token.getToken().length() - 10)));
        saveToken(token);
    }
}


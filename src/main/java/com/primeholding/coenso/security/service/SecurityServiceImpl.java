package com.primeholding.coenso.security.service;

import com.primeholding.coenso.configuration.JsonWebTokenConfiguration;
import com.primeholding.coenso.exception.WrongCredentialsException;
import com.primeholding.coenso.security.JsonWebTokenProvider;
import com.primeholding.coenso.security.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private AuthenticationManager authenticationManager;
    private JsonWebTokenProvider jsonWebTokenProvider;
    private JsonWebTokenConfiguration configuration;

    @Autowired
    public SecurityServiceImpl(AuthenticationManager authenticationManager, JsonWebTokenProvider jsonWebTokenProvider, JsonWebTokenConfiguration configuration) {
        this.authenticationManager = authenticationManager;
        this.jsonWebTokenProvider = jsonWebTokenProvider;
        this.configuration = configuration;
    }

    @Override
    public String authenticate(UserCredentials userCredentials) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userCredentials.getEmail(),
                    userCredentials.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return String.format("%s %s",
                    configuration.getType(),
                    jsonWebTokenProvider.generateToken(authentication));
        } catch (AuthenticationException e) {
            throw new WrongCredentialsException("Credential details are wrong");
        }
    }
}

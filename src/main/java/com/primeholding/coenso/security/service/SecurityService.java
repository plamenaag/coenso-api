package com.primeholding.coenso.security.service;

import com.primeholding.coenso.security.UserCredentials;

public interface SecurityService {
    String authenticate(UserCredentials userCredentials);
}

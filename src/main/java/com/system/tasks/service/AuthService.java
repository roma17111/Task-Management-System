package com.system.tasks.service;

import com.system.tasks.security.JwtRequest;
import com.system.tasks.security.JwtResponse;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;

public interface AuthService {
    JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException;

    JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException;

    JwtResponse refresh(@NonNull String refreshToken) throws AuthException;
}

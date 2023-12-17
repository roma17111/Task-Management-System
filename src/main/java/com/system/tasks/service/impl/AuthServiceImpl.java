package com.system.tasks.service.impl;

import com.system.tasks.entity.TaskUser;
import com.system.tasks.repository.TaskUserRepository;
import com.system.tasks.security.JwtAuthentication;
import com.system.tasks.security.JwtProvider;
import com.system.tasks.security.JwtRequest;
import com.system.tasks.security.JwtResponse;
import com.system.tasks.service.AuthService;
import com.system.tasks.service.TaskUserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TaskUserRepository userRepository;
    private final TaskUserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;


    public List<TaskUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final TaskUser user = userRepository.findByEmail(authRequest.getLogin());
        if (user == null) {
            throw new AuthException("Пользователь " + authRequest.getLogin() + " не найден");
        }
        if (encoder.matches(authRequest.getPassword(),user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final TaskUser user = userRepository.findByEmail(login);
                if (user == null) {
                    throw new AuthException("Пользователь  не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    @Override
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final TaskUser user = userRepository.findByEmail(login);
                if (user == null) {
                    throw new AuthException("Пользователь  не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}

package com.system.tasks.controllers;

import com.system.tasks.dto.RegisterUserDto;
import com.system.tasks.exception.IncorrectPasswordException;
import com.system.tasks.exception.InputDataException;
import com.system.tasks.exception.InvalidMailException;
import com.system.tasks.security.JwtRequest;
import com.system.tasks.security.JwtResponse;
import com.system.tasks.security.RefreshJwtRequest;
import com.system.tasks.service.AuthService;
import com.system.tasks.service.TaskUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final TaskUserService taskUserService;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseBody
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вы успешно зарегистрировались"),
            @ApiResponse(responseCode = "400", description = "Логин уже занят")
    })
    public ResponseEntity<?> register(@RequestBody RegisterUserDto dto) {
        try {
            taskUserService.register(dto);
        } catch (InputDataException e) {
            return ResponseEntity.badRequest().body("Некорректные данные");
        } catch (InvalidMailException e) {
            return ResponseEntity.badRequest().body("Некорректный email");
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.badRequest().body("Пароль должен быть не меньше 5 символов");
        }
        return ResponseEntity.ok("Вы успешно зарегистрировались");
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизоваться",
            description = "Данные контроллер принимает refresh token и возвращает" +
                    " новуый access token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка авторизвции")
    })
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token;
        try {
            token = authService.login(authRequest);
        } catch (AuthException e) {
            return ResponseEntity.status(400).body("Ошабка входа");
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    @Operation(summary = "Получить новый access токен",
            description = "Данные контроллер принимает refresh token и возвращает" +
                    " новуый access token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.getAccessToken(request.getRefreshToken());
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Получить новую пару access and refresh токенов",
            description = "Данные контроллер принимает refresh token и возвращает новую пару токенов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.refresh(request.getRefreshToken());
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

}

package com.system.tasks.service;

import com.system.tasks.dto.RegisterUserDto;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.repository.TaskUserRepository;
import com.system.tasks.security.JwtProvider;
import com.system.tasks.security.JwtRequest;
import com.system.tasks.security.Role;
import com.system.tasks.service.impl.AuthServiceImpl;
import com.system.tasks.service.impl.TaskUserServiceImpl;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private TaskUserRepository userRepository;

    @Mock
    private TaskUserServiceImpl taskUserService;

    @Spy
    private PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Mock
    private JwtProvider jwtProvider;

    @Test
    @SneakyThrows
    void testLogin() {

        String email = "roma@mail.ru";
        String pass = "12345678";


        TaskUser user1 = TaskUser.builder()
                .id(1)
                .email(email)
                .password(new BCryptPasswordEncoder(12).encode(pass))
                .firstName("Roman")
                .lastName("Yakimenko")
                .secondName("edikovic")
                .rolesSet(Set.of(Role.USER))
                .build();

        JwtRequest request = new JwtRequest(email, pass);
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        Assertions.assertNotNull(authService.login(request));

    }

    @Test
    @SneakyThrows
    void testIncorrectPassword() {

        String email = "roma@mail.ru";
        String pass = "12345678";

        TaskUser user1 = TaskUser.builder()
                .id(1)
                .email(email)
                .password(new BCryptPasswordEncoder(12).encode(pass))
                .firstName("Roman")
                .lastName("Yakimenko")
                .secondName("edikovic")
                .rolesSet(Set.of(Role.USER))
                .build();

        JwtRequest request = new JwtRequest(email, "11111111111111111111111111");
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        Assertions.assertThrows(AuthException.class, () -> {
            authService.login(request);
        });
    }

}

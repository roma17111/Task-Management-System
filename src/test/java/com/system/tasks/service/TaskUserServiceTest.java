package com.system.tasks.service;

import com.system.tasks.dto.RegisterUserDto;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.exception.IncorrectPasswordException;
import com.system.tasks.exception.InputDataException;
import com.system.tasks.exception.InvalidMailException;
import com.system.tasks.repository.TaskUserRepository;
import com.system.tasks.security.Role;
import com.system.tasks.service.impl.TaskUserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskUserServiceTest {

    @Mock
    private TaskUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TaskUserService userService = new TaskUserServiceImpl(userRepository, passwordEncoder);


    @Test
    void invalidFioTestException() {
        RegisterUserDto userDto = RegisterUserDto.builder()
                .firstName("12345")
                .lastName("Test2142")
                .build();
        Assertions.assertThrows(InputDataException.class, () -> {
            userService.register(userDto);
        });
    }

    @Test
    void invalidEmailExceptionTest() {
        RegisterUserDto userDto = RegisterUserDto.builder()
                .firstName("Roman")
                .lastName("Yakimenko")
                .secondName("Edikowich")
                .password("1234567575")
                .email("teteteetete")
                .build();
        Assertions.assertThrows(InvalidMailException.class, () -> {
            userService.register(userDto);
        });
    }

    @Test
    void invalidPasswordExceptionTest() {
        RegisterUserDto userDto = RegisterUserDto.builder()
                .firstName("Roman")
                .lastName("Yakimenko")
                .secondName("Edikowich")
                .password("1")
                .email("roma@mail.ru")
                .build();
        Assertions.assertThrows(IncorrectPasswordException.class, () -> {
            userService.register(userDto);
        });
    }

    @Test
    @SneakyThrows
    void emailAlreadyExistsRegisterTaskUserTest() {

        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .firstName("Roman")
                .lastName("Yakimenko")
                .secondName("Edikowich")
                .password("121212121")
                .email("roma@mail.ru")
                .build();

        String password = registerUserDto.getPassword();
        TaskUser user1 = TaskUser.builder()
                .id(1)
                .email(registerUserDto.getEmail())
                .password(password)
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .secondName(registerUserDto.getSecondName())
                .rolesSet(Set.of(Role.USER))
                .build();


        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        TaskUser user = userRepository.findByEmail(user1.getEmail());
        System.out.println(user);


        Assertions.assertThrows(InvalidMailException.class, () -> {
            userService.register(registerUserDto);
        });

    }
}

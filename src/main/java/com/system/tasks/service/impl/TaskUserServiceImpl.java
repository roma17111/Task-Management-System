package com.system.tasks.service.impl;

import com.system.tasks.dto.RegisterUserDto;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.exception.IncorrectPasswordException;
import com.system.tasks.exception.InputDataException;
import com.system.tasks.exception.InvalidMailException;
import com.system.tasks.repository.TaskUserRepository;
import com.system.tasks.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUserServiceImpl implements TaskUserService {

    private final TaskUserRepository taskUserRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private boolean isValidData(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (Character.isDigit(data.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidFio(RegisterUserDto user) {
        if (!isValidData(user.getFirstName()) ||
                !isValidData(user.getLastName()) ||
                !isValidData(user.getSecondName())) {
            return false;
        }
        return true;
    }

    @Override
    public void register(RegisterUserDto registerUserDto) throws InputDataException,
            InvalidMailException,
            IncorrectPasswordException {
        if (!isValidFio(registerUserDto)) {
            throw new InputDataException("Некоррекнтные данные");
        }
        if (!isValidEmail(registerUserDto.getEmail())) {
            throw new InvalidMailException("Incorrect email");
        }
        if (registerUserDto.getPassword().length() < 5) {
            throw new IncorrectPasswordException("Пароль далжен содержать больше 5 символо");
        }
        String password = passwordEncoder.encode(registerUserDto.getPassword());

        TaskUser user = TaskUser.builder()
                .email(registerUserDto.getEmail())
                .password(password)
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .secondName(registerUserDto.getSecondName())
                .build();

        taskUserRepository.save(user);


    }
}

package com.system.tasks.service.impl;

import com.system.tasks.dto.RegisterUserDto;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.exception.IncorrectPasswordException;
import com.system.tasks.exception.InputDataException;
import com.system.tasks.exception.InvalidMailException;
import com.system.tasks.repository.TaskUserRepository;
import com.system.tasks.security.Role;
import com.system.tasks.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

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
        TaskUser user = taskUserRepository.findByEmail(registerUserDto.getEmail());
        if (user != null) {
            throw new InvalidMailException("email уже занят!");
        }
        String password = passwordEncoder.encode(registerUserDto.getPassword());

        TaskUser user1 = TaskUser.builder()
                .email(registerUserDto.getEmail())
                .password(password)
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .secondName(registerUserDto.getSecondName())
                .rolesSet(Set.of(Role.USER))
                .build();

        taskUserRepository.save(user1);

    }

    @Override
    @Transactional
    public TaskUser findById(long id) {
        Optional<TaskUser> taskUser = taskUserRepository.findById(id);
        if (taskUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return taskUser.get();
    }

    @Override
    public TaskUser getAuthUser() {
        SecurityContext holder = SecurityContextHolder.getContext();
        Authentication authentication = holder.getAuthentication();
        String email = authentication.getName();
        return taskUserRepository.findByEmail(email);
    }
}

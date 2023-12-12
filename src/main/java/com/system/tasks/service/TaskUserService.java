package com.system.tasks.service;

import com.system.tasks.dto.RegisterUserDto;
import com.system.tasks.exception.IncorrectPasswordException;
import com.system.tasks.exception.InputDataException;
import com.system.tasks.exception.InvalidMailException;

public interface TaskUserService {
    void register(RegisterUserDto user) throws InputDataException, InvalidMailException, IncorrectPasswordException;
}

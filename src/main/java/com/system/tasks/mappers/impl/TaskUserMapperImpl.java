package com.system.tasks.mappers.impl;

import com.system.tasks.dto.TaskUserDto;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.mappers.TaskUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUserMapperImpl implements TaskUserMapper {

    @Override
    public TaskUser mapToUser(TaskUserDto userDto) {
        return TaskUser.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .secondName(userDto.getSecondName())
                .email(userDto.getEmail())
                .password("********")
                .build();
    }

    @Override
    public TaskUserDto mapToDto(TaskUser user) {
        return TaskUserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .secondName(user.getSecondName())
                .email(user.getEmail())
                .build();
    }
}

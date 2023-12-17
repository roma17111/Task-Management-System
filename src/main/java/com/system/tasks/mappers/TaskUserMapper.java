package com.system.tasks.mappers;

import com.system.tasks.dto.TaskUserDto;
import com.system.tasks.entity.TaskUser;

public interface TaskUserMapper {
    TaskUser mapToUser(TaskUserDto userDto);

    TaskUserDto mapToDto(TaskUser user);
}

package com.system.tasks.mappers;

import com.system.tasks.dto.TaskDto;
import com.system.tasks.entity.Task;

import java.util.List;

public interface TaskMapper {
    Task mapToTask(TaskDto dto);

    TaskDto mapToDto(Task task);

    List<TaskDto> mapAllToDto(List<Task> tasks);
}

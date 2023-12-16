package com.system.tasks.mappers.impl;

import com.system.tasks.dto.CommentDto;
import com.system.tasks.dto.TaskDto;
import com.system.tasks.dto.TaskUserDto;
import com.system.tasks.entity.Comment;
import com.system.tasks.entity.Task;
import com.system.tasks.entity.TaskUser;
import com.system.tasks.mappers.CommentMapper;
import com.system.tasks.mappers.TaskMapper;
import com.system.tasks.mappers.TaskUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskMapperImpl implements TaskMapper {

    private final CommentMapper commentMapper;
    private final TaskUserMapper userMapper;

    @Override
    public Task mapToTask(TaskDto dto) {
        TaskUser author = userMapper.mapToUser(dto.getAuthor());
        TaskUser executor = userMapper.mapToUser(dto.getExecutor());
        List<Comment> comments = commentMapper.mapAllToComment(dto.getComments());
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .taskStatus(dto.getTaskStatus())
                .taskPriority(dto.getTaskPriority())
                .author(author)
                .executor(executor)
                .comments(comments)
                .build();
    }

    @Override
    public TaskDto mapToDto(Task task) {
        TaskUserDto author = userMapper.mapToDto(task.getAuthor());
        TaskUserDto executor = userMapper.mapToDto(task.getExecutor());
        List<CommentDto> comments = commentMapper.mapAllToCommentDto(task.getComments());
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .taskStatus(task.getTaskStatus())
                .taskPriority(task.getTaskPriority())
                .author(author)
                .executor(executor)
                .comments(comments)
                .build();
    }
}

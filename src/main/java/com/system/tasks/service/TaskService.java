package com.system.tasks.service;

import com.system.tasks.dto.CommentDto;
import com.system.tasks.dto.CreateTaskDto;
import com.system.tasks.dto.EditTaskDto;
import com.system.tasks.dto.TaskDto;
import com.system.tasks.entity.Task;
import com.system.tasks.exception.EditTaskException;
import com.system.tasks.exception.TaskInProcessException;
import jakarta.security.auth.message.AuthException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskService {
    @Transactional
    void save(Task task);

    @Transactional
    List<Task> getTasks(int pageNumber, int size);

    @Transactional
    List<Task> getTaskByAuthor(int pageNumber, int size) throws AuthException;

    Task findById(long id);

    void createTask(CreateTaskDto taskDto) throws AuthException;

    void deleteTaskById(long id) throws AuthException, TaskInProcessException;

    @Transactional
    TaskDto editTask(long id, EditTaskDto editTaskDto) throws EditTaskException;

    void addExecutorToTask(long taskId, long authorId) throws AuthException, EditTaskException;

    @Transactional
    void deleteExecutorToTask(long taskId) throws AuthException, EditTaskException;

    @Transactional
    void addCommentToTask(long taskId, CommentDto commentDto) throws AuthException, EditTaskException;
}
